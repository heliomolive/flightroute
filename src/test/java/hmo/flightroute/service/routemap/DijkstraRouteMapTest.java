package hmo.flightroute.service.routemap;


import hmo.flightroute.domain.Route;
import hmo.flightroute.domain.exception.InternalErrorException;
import hmo.flightroute.domain.exception.UnprocessableEntityException;
import hmo.flightroute.mother.DijsktraRouteMapMother;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static hmo.flightroute.mother.DijsktraRouteMapMother.N_VERTICES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraRouteMapTest {

    private DijkstraRouteMap fixture;

    @Test
    public void testAddRouteAndGetVertexNumber() {
        //given:
        initializeRouteMap(N_VERTICES+1);
        String newDest = String.valueOf(N_VERTICES);

        //when:
        fixture.addRoute(new Route("0", newDest, 15));
        Optional<Integer> newVertex = fixture.getVertexNumber(newDest);

        //then:
        assertTrue(newVertex.isPresent());
        assertEquals(N_VERTICES, newVertex.get());
        assertEquals(0f, fixture.findRoute(newDest, newDest).getCost());
    }

    @Test
    public void testAddRouteWhenRouteMapIsFull() {
        //given:
        initializeRouteMap(N_VERTICES);
        Route newRoute = new Route("0", "newDest", 13f);

        //then:
        assertThrows(InternalErrorException.class, () -> fixture.addRoute(newRoute));
    }

    @Test
    public void testGetRouteWhenLocationIsInvalid() {
        //given:
        initializeRouteMap(N_VERTICES);

        //then:
        assertThrows(UnprocessableEntityException.class, () -> fixture.findRoute("0", "fake"));
        assertThrows(UnprocessableEntityException.class, () -> fixture.findRoute("fake", "0"));
    }

    @Test
    public void testDistanceFrom0to8() {
        //given:
        initializeRouteMap(N_VERTICES);
        String src = "0";
        String dest = "8";

        //when:
        Route route = fixture.findRoute(src, dest);

        //then:
        assertNotNull(route);
        assertEquals(src, route.getSource());
        assertEquals(dest, route.getDest());
        assertEquals(14f, route.getCost());
        assertEquals(4, route.getConnections().size());
        assertEquals("0", route.getConnections().get(0));
        assertEquals("1", route.getConnections().get(1));
        assertEquals("2", route.getConnections().get(2));
        assertEquals("8", route.getConnections().get(3));
    }

    @Test
    public void testDistanceFrom3to7() {
        //given:
        initializeRouteMap(N_VERTICES);
        String src = "3";
        String dest = "7";

        //when:
        Route route = fixture.findRoute(src, dest);

        //then:
        assertNotNull(route);
        assertEquals(src, route.getSource());
        assertEquals(dest, route.getDest());
        assertEquals(14f, route.getCost());
        assertEquals(5, route.getConnections().size());
        assertEquals("3", route.getConnections().get(0));
        assertEquals("2", route.getConnections().get(1));
        assertEquals("5", route.getConnections().get(2));
        assertEquals("6", route.getConnections().get(3));
        assertEquals("7", route.getConnections().get(4));
    }

    private void initializeRouteMap(int vertices) {
        fixture = DijsktraRouteMapMother.getRouteMapWithDirectRoutes(vertices);
    }

}