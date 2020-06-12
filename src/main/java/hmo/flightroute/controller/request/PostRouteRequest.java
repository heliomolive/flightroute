package hmo.flightroute.controller.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PostRouteRequest {

    @NotEmpty(message = "Required parameter: source")
    private String source;

    @NotEmpty(message = "Required parameter: dest")
    private String dest;

    @NotNull(message = "Required parameter: cost")
    private BigDecimal cost;

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
}
