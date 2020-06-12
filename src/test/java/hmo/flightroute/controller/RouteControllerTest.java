package hmo.flightroute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hmo.flightroute.controller.request.PostRouteRequest;
import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.exception.NotFoundException;
import hmo.flightroute.mother.PostRouteRequestMother;
import hmo.flightroute.service.RouteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ControllerTestConfig.class)
class RouteControllerTest {

    private static final String ROUTE_URI = "/v1/route";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RouteService routeService;


    @Test
    public void getRoute() throws Exception {
        //given
        Route route = new Route("v0", "v3", 250f, Arrays.asList("v0", "v1", "v2", "v3"));
        when(routeService.findRoute(route.getSource(), route.getDest())).thenReturn(route);

        //then:
        mockMvc.perform(get(ROUTE_URI + "?source=v0&dest=v3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value(route.getSource()))
                .andExpect(jsonPath("$.dest").value(route.getDest()))
                .andExpect(jsonPath("$.cost").value(route.getCost()))
                .andExpect(jsonPath("$.connections.[0]").value("v0"))
                .andExpect(jsonPath("$.connections.[1]").value("v1"))
                .andExpect(jsonPath("$.connections.[2]").value("v2"))
                .andExpect(jsonPath("$.connections.[3]").value("v3"));

        verify(routeService).findRoute(route.getSource(), route.getDest());
    }

    @Test
    public void getRouteWhenSourceIsInvalid() throws Exception {
        //given:
        String developerMessage = "developer message";
        String userMessage = "user message";
        when(routeService.findRoute(anyString(), anyString())).thenThrow(
                new NotFoundException(developerMessage, userMessage));

        //then:
        mockMvc.perform(get(ROUTE_URI + "?source=v0&dest=v3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.developerMessage").value(developerMessage))
                .andExpect(jsonPath("$.userMessage").value(userMessage));

        verify(routeService).findRoute("v0", "v3");
    }

    @Test
    public void postRoute() throws Exception {
        //given:
        PostRouteRequest request = PostRouteRequestMother.getPostRouteRequest("v1", "v5", new BigDecimal(100));
        when(routeService.createRoute(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(new Route("v1", "v5", 100f));

        //then:
        mockMvc.perform(post(ROUTE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(routeService).createRoute("v1", "v5", new BigDecimal(100));
    }

}