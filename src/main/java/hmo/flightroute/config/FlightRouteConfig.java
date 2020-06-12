package hmo.flightroute.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@EnableCaching
@PropertySources({
        @PropertySource(value = "classpath:/application.properties", encoding = "UTF-8"),
        @PropertySource(value = "classpath:/messages.properties", encoding = "UTF-8")
})
public class FlightRouteConfig {
}
