package hmo.flightroute.data;

import hmo.flightroute.util.UserMessageLoader;
import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.enums.UserMessage;
import hmo.flightroute.domain.exception.InternalErrorException;
import hmo.flightroute.service.routemap.DijkstraRouteMap;
import hmo.flightroute.service.routemap.RouteMap;
import hmo.flightroute.domain.exception.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class InputRouteFileLoader {

    private static final Logger logger = LoggerFactory.getLogger(InputRouteFileLoader.class.getName());
    private static final String CSV_COLUMN_MARKER = ",";

    @Autowired
    private UserMessageLoader userMessageLoader;

    @Value("${input.file}")
    private String inputRouteFile;

    @Value("${input.file.encoding}")
    private String inputRouteFileEncoding;


    public RouteMap loadInputFile() {

        Map<String, Map<String, Route>> routes = new HashMap<>();

        try(FileReader fileReader = new FileReader()) {
            fileReader.open(inputRouteFile, inputRouteFileEncoding);

            String line = fileReader.readLine();
            while (nonNull(line)) {
                parseRouteLine(line).ifPresent(route -> updateRoutes(routes, route));
                line = fileReader.readLine();
            }

            logger.info("Finished loading input routes file. [{}] destinations found.", routes.size());
        } catch (Exception e) {
            logger.error("Error loading input file [{}] with encoding [{}].",
                    inputRouteFile, inputRouteFileEncoding, e);
            throw new UnprocessableEntityException(
                    format("Error loading input file [%s] with encoding [%s]", inputRouteFile, inputRouteFileEncoding),
                    format(userMessageLoader.getUserMessage(UserMessage.INPUT_ROUTE_FILE_ERROR), inputRouteFileEncoding),
                    e);
        }

        return convertToRouteMap(routes);
    }

    public void appendNewRoute(Route route) {
        try(FileWriter writer = new FileWriter()) {
            writer.open(inputRouteFile, inputRouteFileEncoding, true);

            String newLine = new StringBuilder()
                    .append(route.getSource())
                    .append(CSV_COLUMN_MARKER)
                    .append(route.getDest())
                    .append(CSV_COLUMN_MARKER)
                    .append(route.getCost()).toString();

            writer.writeLine(newLine);
            logger.info("New route line [{}] appended to route file [{}]", newLine, inputRouteFile);

        } catch (Exception e) {
            logger.error("Error appending new route line to file [{}] with encoding [{}].",
                    inputRouteFile, inputRouteFileEncoding, e);
            throw new InternalErrorException(
                    format("Error appending new route line to input file [%s] with encoding [%s]", inputRouteFile, inputRouteFileEncoding),
                    userMessageLoader.getUserMessage(UserMessage.INPUT_ROUTE_FILE_UPDATE_ERROR),
                    e);
        }
    }

    private DijkstraRouteMap convertToRouteMap(Map<String, Map<String, Route>> routes) {
        DijkstraRouteMap routeMap = new DijkstraRouteMap(routes.size());
        int count = 0;

        for (Map<String, Route> sourceRoutes : routes.values()) {
            for (Route route : sourceRoutes.values()) {
                logger.info("Route loaded from input file: {}-{}: {}", route.getSource(), route.getDest(), route.getCost());
                routeMap.addRoute(route);
                count++;
            }
        }

        logger.info("[{}] routes loaded from input file", count);
        return routeMap;
    }

    private void updateRoutes(Map<String, Map<String, Route>> routes, Route newRoute) {
        if (!routes.containsKey(newRoute.getSource())) {
            routes.put(newRoute.getSource(), new HashMap<>());
        }
        if (!routes.containsKey(newRoute.getDest())) {
            routes.put(newRoute.getDest(), new HashMap<>());
        }

        Map<String, Route> routesFromSource = routes.get(newRoute.getSource());
        if (routesFromSource.containsKey(newRoute.getDest())) {
            logger.warn("Duplicate route ignored (source [{}], destination [{}])", newRoute.getSource(), newRoute.getDest());
            return;
        }
        routesFromSource.put(newRoute.getDest(), newRoute);
    }

    private Optional<Route> parseRouteLine(String line) {
        String[] route = nonNull(line) ? line.split(CSV_COLUMN_MARKER) : null;
        if (isNull(route)) {
            return Optional.empty();
        }

        String src = route[0].toUpperCase();
        String dest = route[1].toUpperCase();
        BigDecimal cost = new BigDecimal(route[2]).setScale(2, RoundingMode.FLOOR);
        float costAsFloat = cost.floatValue();

        if (src.length()>0 && dest.length()>0 && !src.equals(dest) && costAsFloat>0f) {
            return Optional.of(new Route(src, dest, costAsFloat));
        }

        logger.warn("Invalid input file line ignored: [{}]", line);
        return Optional.empty();
    }

}
