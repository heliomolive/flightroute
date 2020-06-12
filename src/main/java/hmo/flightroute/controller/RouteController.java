package hmo.flightroute.controller;

import hmo.flightroute.controller.request.PostRouteRequest;
import hmo.flightroute.controller.response.FlightRouteResponse;
import hmo.flightroute.domain.mapper.RouteMapper;
import hmo.flightroute.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RouteController {

    private static final String V1 = "/v1";

    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteMapper routeMapper;

    @GetMapping(V1 + "/route")
    @ResponseStatus(HttpStatus.OK)
    public FlightRouteResponse findRoute(@RequestParam String source, @RequestParam String dest) {
        return routeMapper.getGetRouteResponse(
                routeService.findRoute(source, dest));
    }

    @PostMapping(V1 + "/route")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRoute(@RequestBody @Valid PostRouteRequest request) {
        routeService.createRoute(request.getSource(), request.getDest(), request.getCost());
    }
}
