package hmo.flightroute.domain.mapper;

import hmo.flightroute.controller.response.GetRouteResponse;
import hmo.flightroute.domain.Route;

public interface RouteMapper {

    GetRouteResponse getGetRouteResponse(Route route);
}
