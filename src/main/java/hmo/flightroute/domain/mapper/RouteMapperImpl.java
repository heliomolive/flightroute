package hmo.flightroute.domain.mapper;

import hmo.flightroute.controller.response.GetRouteResponse;
import hmo.flightroute.domain.Route;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.nonNull;

@Component
public class RouteMapperImpl implements RouteMapper {

    @Override
    public GetRouteResponse getGetRouteResponse(Route route) {
        GetRouteResponse getRouteResponse = new GetRouteResponse();
        if (nonNull(route)) {
            getRouteResponse.setSource(route.getSource());
            getRouteResponse.setDest(route.getDest());
            getRouteResponse.setCost(BigDecimal.valueOf(route.getCost()).setScale(2, RoundingMode.FLOOR));
            getRouteResponse.setConnections(route.getConnections());
        }
        return getRouteResponse;
    }
}
