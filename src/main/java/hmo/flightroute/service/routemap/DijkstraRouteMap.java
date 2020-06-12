package hmo.flightroute.service.routemap;

import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.exception.InternalErrorException;
import hmo.flightroute.domain.exception.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.Float.MAX_VALUE;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DijkstraRouteMap implements RouteMap {

    private static final Logger logger = LoggerFactory.getLogger(DijkstraRouteMap.class.getName());

    private Map<String, Integer> verticeNumber;
    private Route[][] routes;

    public DijkstraRouteMap(int numberOfVertices) {
        verticeNumber = new HashMap<>(numberOfVertices);
        routes = new Route[numberOfVertices][numberOfVertices];
    }

    public Optional<Integer> getVertexNumber(String location) {
        return Optional.ofNullable(verticeNumber.get(location));
    }

    public void addRoute(Route route) {
        Integer srcVertice = verticeNumber.containsKey(route.getSource()) ?
                verticeNumber.get(route.getSource()) : addNewVertex(route.getSource());
        Integer destVertice = verticeNumber.containsKey(route.getDest()) ?
                verticeNumber.get(route.getDest()) : addNewVertex(route.getDest());;

        routes[srcVertice][destVertice] = route;
    }

    public Route findRoute(String source, String dest) {
        Integer srcVertice = verticeNumber.get(source);
        Integer destVertice = verticeNumber.get(dest);

        if (isNull(srcVertice) || isNull(destVertice)) {
            throw new UnprocessableEntityException(format("Invalid source [%s] or destination [%s].", source, dest));
        }

        Route route = routes[srcVertice][destVertice];
        return nonNull(route) && route.isCostVerified() ?
                route : findCheapestRoute(srcVertice, destVertice);
    }

    @Override
    public boolean containsDirectRoute(String source, String dest) {
        Integer srcVertice = verticeNumber.get(source);
        Integer destVertice = verticeNumber.get(dest);
        if (nonNull(srcVertice) && nonNull(destVertice)) {
            Route route = routes[srcVertice][destVertice];
            return nonNull(route) && route.getConnections().isEmpty();
        }
        return false;
    }

    private int addNewVertex(String location) {
        int newVertex = verticeNumber.size();
        if (newVertex>=routes.length) {
            throw new InternalErrorException("Max number of vertices achieved, please recreate the route map.");
        }
        routes[newVertex][newVertex] = new Route(location, location, 0f);
        verticeNumber.put(location, newVertex);
        return newVertex;
    }

    private Route findCheapestRoute(int srcIndex, int destIndex) {
        Route route = dijkstraForSingleTarget(srcIndex, destIndex);
        route.setCostVerified(true);
        routes[srcIndex][destIndex] = route;

        logger.debug("New route calculated: {}", route);
        return route;
    }

    private Route dijkstraForSingleTarget(int srcIndex, int destIndex) {
        int vertices = verticeNumber.size();
        Route[] cheapestFromSrc = routesWithMaxCost(vertices); //cost from srcIndex to each other indexes
        boolean[] spt = new boolean[vertices]; //shortest path tree (finished vertices)

        cheapestFromSrc[srcIndex] = routes[srcIndex][srcIndex]; //every new vertex is add into routes with zero cost route

        for (int count=0; count<vertices-1; count++) {

            int vertexI = cheapestVertexNotIncludedInSpt(cheapestFromSrc, spt);
            spt[vertexI] = true;
            Route routeI = cheapestFromSrc[vertexI];

            if (vertexI==destIndex) {
                return routeI; //Dijkstra single-target implementation: stop when target is reached
            }

            for (int v=0; v<vertices; v++) {
                if (!spt[v]                             //not included in short path yet
                        && nonNull(routes[vertexI][v])  //dist from vertexI to v is know
                        && routeI.getCost() + routes[vertexI][v].getCost() < cheapestFromSrc[v].getCost() //cost until v is smaller than the cheapest
                ) {
                    String distV = routes[v][v].getSource();
                    Route routeUntilV = new Route(routeI.getSource(), distV,
                            routeI.getCost() + routes[vertexI][v].getCost(), routeI.getConnections());
                    cheapestFromSrc[v] = routeUntilV;
                }
            }
        }

        return cheapestFromSrc[destIndex];
    }

    private Route[] routesWithMaxCost(int vertices) {
        Route[] routes = new Route[vertices];
        for (int i=0; i<vertices; i++) {
            routes[i] = new Route(null, null, MAX_VALUE);
        }
        return routes;
    }

    private int cheapestVertexNotIncludedInSpt(Route[] cheapestFromSrc, boolean[] spt) {
        float cheapestCost = MAX_VALUE;
        int cheapestVertex = -1;

        for (int v=0; v<cheapestFromSrc.length; v++) {
            Route routeI = cheapestFromSrc[v];
            if (!spt[v] && routeI.getCost()<cheapestCost) {
                cheapestCost = routeI.getCost();
                cheapestVertex = v;
            }
        }
        return cheapestVertex;
    }

}
