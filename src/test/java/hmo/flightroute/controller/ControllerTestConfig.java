package hmo.flightroute.controller;

import hmo.flightroute.domain.mapper.RouteMapper;
import hmo.flightroute.domain.mapper.RouteMapperImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerTestConfig {

    @Bean
    @ConditionalOnMissingBean
    public RouteMapper getRouteMapper() {
        return new RouteMapperImpl();
    }
}
