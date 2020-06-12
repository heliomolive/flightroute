package hmo.flightroute.util;

import hmo.flightroute.domain.enums.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static hmo.flightroute.domain.enums.UserMessage.INPUT_ROUTE_FILE_ERROR;
import static hmo.flightroute.domain.enums.UserMessage.INPUT_ROUTE_FILE_UPDATE_ERROR;
import static hmo.flightroute.domain.enums.UserMessage.INVALID_COST_FOR_ROUTE;
import static hmo.flightroute.domain.enums.UserMessage.INVALID_DEST;
import static hmo.flightroute.domain.enums.UserMessage.INVALID_DEST_FOR_NEW_ROUTE;
import static hmo.flightroute.domain.enums.UserMessage.INVALID_SCR;
import static hmo.flightroute.domain.enums.UserMessage.INVALID_SRC_FOR_NEW_ROUTE;
import static hmo.flightroute.domain.enums.UserMessage.ROUTE_ALREADY_REGISTERED;

@Component
public class UserMessageLoader {
    @Value("${flightroute.src.not.found}")
    private String srcNotFoundMessage;

    @Value("${flightroute.dest.not.found}")
    private String destNotFoundMessage;

    @Value("${flightroute.invalid.src.for.new.route}")
    private String invalidSrcForNewRouteMessage;

    @Value("${flightroute.invalid.dest.for.new.route}")
    private String invalidDestForNewRouteMessage;

    @Value("${flightroute.invalid.cost.for.route}")
    private String invalidCostForRouteMessage;

    @Value("${flightroute.route.already.registered}")
    private String routeAlreadyRegisteredMessage;

    @Value("${flightroute.input.route.file.error}")
    private String inputRouteFileErrorMessage;

    @Value("${flightroute.input.route.file.update.error}")
    private String inputRouteFileUpdateErrorMessage;

    private Map<UserMessage, String> userMessage;

    @PostConstruct
    public void setup() {
        userMessage = new HashMap<>();
        userMessage.put(INVALID_SCR, srcNotFoundMessage);
        userMessage.put(INVALID_DEST, destNotFoundMessage);
        userMessage.put(INVALID_SRC_FOR_NEW_ROUTE, invalidSrcForNewRouteMessage);
        userMessage.put(INVALID_DEST_FOR_NEW_ROUTE, invalidDestForNewRouteMessage);
        userMessage.put(INVALID_COST_FOR_ROUTE, invalidCostForRouteMessage);
        userMessage.put(ROUTE_ALREADY_REGISTERED, routeAlreadyRegisteredMessage);
        userMessage.put(INPUT_ROUTE_FILE_ERROR, inputRouteFileErrorMessage);
        userMessage.put(INPUT_ROUTE_FILE_UPDATE_ERROR, inputRouteFileUpdateErrorMessage);
    }

    public String getUserMessage(UserMessage userMessage) {
        return this.userMessage.get(userMessage);
    }
}
