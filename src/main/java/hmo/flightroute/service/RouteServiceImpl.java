package hmo.flightroute.service;

import hmo.flightroute.util.UserMessageLoader;
import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.enums.UserMessage;
import hmo.flightroute.domain.exception.UnprocessableEntityException;
import hmo.flightroute.service.routemap.RouteMap;
import hmo.flightroute.domain.exception.NotFoundException;
import hmo.flightroute.data.InputRouteFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static java.lang.String.format;

@Service
public class RouteServiceImpl implements RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class.getName());

    @Autowired
    private InputRouteFileLoader inputRouteFileLoader;

    @Autowired
    private UserMessageLoader userMessageLoader;

    private RouteMap routeMap;


    @PostConstruct
    public void initializeRouteMap() {
        logger.info("Route Service setup...");
        routeMap = inputRouteFileLoader.loadInputFile();
    }

    @Override
    public Route findRoute(String source, String destination) {
        logger.debug("Finding best route option from [{}] to [{}]", source, destination);
        String src = source.toUpperCase();
        if (routeMap.getVertexNumber(src).isEmpty()) {
            throw new NotFoundException(format("Invalid source [%s]", source),
                    format(userMessageLoader.getUserMessage(UserMessage.INVALID_SCR), source));
        }

        String dest = destination.toUpperCase();
        if (routeMap.getVertexNumber(dest).isEmpty()) {
            throw new NotFoundException(format("Invalid destination [%s]", destination),
                    format(userMessageLoader.getUserMessage(UserMessage.INVALID_DEST), destination));
        }

        return routeMap.findRoute(src, dest);
    }

    @Override
    public Route createRoute(String source, String dest, BigDecimal cost) {
        logger.debug("Registering new direct route: {}-{},{}", source, dest, cost.toString());
        source = source.toUpperCase();
        dest = dest.toUpperCase();

        Route route = validateNewRoute(source, dest, cost); //1. validate the new route

        inputRouteFileLoader.appendNewRoute(route);         //2. update route file

        logger.info("Input route file was updated, it is necessary to reload it");
        initializeRouteMap();                               //3. resstart route map

        return route;
    }

    private Route validateNewRoute(String src, String dest, BigDecimal cost) {
        src = src.toUpperCase();
        dest = dest.toUpperCase();

        validateNewRouteSrc(src);
        validateNewRouteDest(src, dest);
        validateNewRouteCost(cost);
        validateNewRouteAlreadyRegistered(src, dest);

        return new Route(src, dest, cost.floatValue());
    }

    private void validateNewRouteSrc(String src) {
        if (routeMap.getVertexNumber(src).isEmpty()) {
            throw new UnprocessableEntityException(format("Origin [%s] does not exist", src),
                    userMessageLoader.getUserMessage(UserMessage.INVALID_SRC_FOR_NEW_ROUTE));
        }
    }

    private void validateNewRouteDest(String src, String dest) {
        if (src.equals(dest)) {
            throw new UnprocessableEntityException(format("Dest [%s] can not be equal to source for new routes", src),
                    userMessageLoader.getUserMessage(UserMessage.INVALID_DEST_FOR_NEW_ROUTE));
        }
    }

    private void validateNewRouteCost(BigDecimal cost) {
        if (BigDecimal.ZERO.compareTo(cost)>=0) {
            throw new UnprocessableEntityException(format("Invalid cost [%s] for new route", cost),
                    userMessageLoader.getUserMessage(UserMessage.INVALID_COST_FOR_ROUTE));
        }
    }

    private void validateNewRouteAlreadyRegistered(String src, String dest) {
        if (routeMap.containsDirectRoute(src, dest)) {
            throw new UnprocessableEntityException(format("Route [%s]-[%s] already registered", src, dest),
                    format(userMessageLoader.getUserMessage(UserMessage.ROUTE_ALREADY_REGISTERED), src, dest));
        }
    }

}
