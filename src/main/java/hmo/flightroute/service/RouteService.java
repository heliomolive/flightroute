package hmo.flightroute.service;

import hmo.flightroute.domain.Route;

import java.math.BigDecimal;

public interface RouteService {

    Route findRoute(String source, String dest);

    Route createRoute(String source, String dest, BigDecimal cost);
}
