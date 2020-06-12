package hmo.flightroute.mother;

import hmo.flightroute.service.routemap.DijkstraRouteMap;
import hmo.flightroute.domain.Route;

public class DijsktraRouteMapMother {

    public static final int N_VERTICES;

    private static int[][] distances;

    static {
        distances = new int[][] {
                { 0, 4, 0, 0, 0, 0, 0, 8, 0 },
                { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
                { 0, 8, 0, 7, 0, 4, 0, 0, 2 },
                { 0, 0, 7, 0, 9, 14, 0, 0, 0 },
                { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
                { 0, 0, 4, 14, 10, 0, 2, 0, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 1, 6 },
                { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
                { 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
        N_VERTICES = distances.length;
    }

    public static DijkstraRouteMap getRouteMapWithDirectRoutes() {
        return getRouteMap(distances.length);
    }

    public static DijkstraRouteMap getRouteMapWithDirectRoutes(int vertices) {
        return getRouteMap(vertices);
    }

    private static DijkstraRouteMap getRouteMap(int vertices) {
        DijkstraRouteMap routeMap = new DijkstraRouteMap(vertices);

        //first add routes to represent all vertices, so we can guarantee the correct vertices order
        for (int i=0; i<vertices; i++) {
            String location = String.valueOf(i);
            routeMap.addRoute(new Route(location, location, 0));
        }

        //add routes with know distances
        for (int i=0; i<vertices && i<distances.length; i++) {
            String src = String.valueOf(i);
            for (int j=0; j<vertices && j<distances.length; j++) {
                if (distances[i][j]>0) {
                    routeMap.addRoute(new Route(src, String.valueOf(j), distances[i][j]));
                }
            }
        }

        return routeMap;
    }
}
