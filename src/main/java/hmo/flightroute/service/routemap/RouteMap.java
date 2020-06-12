package hmo.flightroute.service.routemap;

import hmo.flightroute.domain.Route;

import java.util.Optional;

public interface RouteMap {

    void addRoute(Route route);

    Route findRoute(String source, String dest);

    boolean containsDirectRoute(String source, String dest);

    Optional<Integer> getVertexNumber(String location);
}
