package hmo.flightroute.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

public class Route {
    private String source;
    private String dest;
    private List<String> connections = new ArrayList<>();
    private float cost;
    private boolean costVerified;

    public Route(String source, String dest, float cost) {
        this(source, dest, cost, Collections.emptyList());
    }

    public Route(String source, String dest, float cost, List<String> connectionsBeforeDest) {
        this.source = source;
        this.dest = dest;
        this.cost = cost;
        if (nonNull(connectionsBeforeDest) && !connectionsBeforeDest.isEmpty()) {
            this.connections.addAll(connectionsBeforeDest);
            this.connections.add(dest);
        } else if (!Objects.equals(source, dest)){
            this.connections.add(source);
            this.connections.add(dest);
        }
    }

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

    public List<String> getConnections() {
        return connections;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public boolean isCostVerified() {
        return costVerified;
    }

    public void setCostVerified(boolean costVerified) {
        this.costVerified = costVerified;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Route{");
        sb.append("source='").append(source).append('\'');
        sb.append(", dest='").append(dest).append('\'');
        sb.append(", connections=").append(connections);
        sb.append(", cost=").append(cost);
        sb.append(", costVerified=").append(costVerified);
        sb.append('}');
        return sb.toString();
    }
}
