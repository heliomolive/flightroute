package hmo.flightroute.mother;

import hmo.flightroute.controller.request.PostRouteRequest;

import java.math.BigDecimal;

public class PostRouteRequestMother {

    public static PostRouteRequest getPostRouteRequest(String src, String dest, BigDecimal cost) {
        PostRouteRequest request = new PostRouteRequest();
        request.setSource(src);
        request.setDest(dest);
        request.setCost(cost);
        return request;
    }
}
