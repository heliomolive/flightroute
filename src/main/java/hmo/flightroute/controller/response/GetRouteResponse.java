package hmo.flightroute.controller.response;

import java.math.BigDecimal;
import java.util.List;

public class GetRouteResponse implements FlightRouteResponse{
    private String source;
    private String dest;
    private BigDecimal cost;
    private List<String> connections;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<String> getConnections() {
        return connections;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }
}
