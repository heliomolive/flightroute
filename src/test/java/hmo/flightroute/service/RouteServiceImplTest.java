package hmo.flightroute.service;

import hmo.flightroute.util.UserMessageLoader;
import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.enums.UserMessage;
import hmo.flightroute.domain.exception.NotFoundException;
import hmo.flightroute.mother.DijsktraRouteMapMother;
import hmo.flightroute.data.InputRouteFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RouteServiceImplTest {

    private static final String INVALID_DEST_USER_MSG = "Destino inválido: %s";
    private static final String INVALID_SRC_USER_MSG = "Origem inválida: %s";

    @InjectMocks
    private RouteServiceImpl fixture;

    @Mock
    private InputRouteFileLoader inputRouteFileLoader;

    @Mock
    private UserMessageLoader userMessageLoader;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findRoute() {
        //given:
        String src = "0";
        String dest = "5";
        initializeRouteMap();

        //when:
        Route route = fixture.findRoute(src, dest);

        //then:
        assertNotNull(route);
        assertEquals(src, route.getSource());
        assertEquals(dest, route.getDest());
        assertEquals(11f, route.getCost());
        assertEquals(4, route.getConnections().size());
        assertEquals("0", route.getConnections().get(0));
        assertEquals("7", route.getConnections().get(1));
        assertEquals("6", route.getConnections().get(2));
        assertEquals("5", route.getConnections().get(3));
    }

    @Test
    public void findRouteWhenSrcIsInvalid() {
        //given:
        initializeRouteMap();
        String src = "fake";
        when(userMessageLoader.getUserMessage(UserMessage.INVALID_SCR)).thenReturn(INVALID_SRC_USER_MSG);

        //when:
        NotFoundException e = assertThrows(
                NotFoundException.class, () -> fixture.findRoute(src, "0"));

        //then:
        assertEquals(format(INVALID_SRC_USER_MSG, src), e.getUserMessage());
        verify(userMessageLoader).getUserMessage(eq(UserMessage.INVALID_SCR));
    }

    @Test
    public void findRouteWhenDestIsInvalid() {
        //given:
        initializeRouteMap();
        String dest = "fake";
        when(userMessageLoader.getUserMessage(UserMessage.INVALID_DEST)).thenReturn(INVALID_DEST_USER_MSG);

        //when:
        NotFoundException e = assertThrows(
                NotFoundException.class, () -> fixture.findRoute("0", dest));

        //then:
        assertEquals(format(INVALID_DEST_USER_MSG, dest), e.getUserMessage());
        verify(userMessageLoader).getUserMessage(eq(UserMessage.INVALID_DEST));
    }

    private void initializeRouteMap() {
        when(inputRouteFileLoader.loadInputFile()).thenReturn(DijsktraRouteMapMother.getRouteMapWithDirectRoutes());
        fixture.initializeRouteMap();
    }

}