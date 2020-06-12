package hmo.flightroute.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightRouteIntTest {

    private static final String ROUTE_URI = "/v1/route";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getRoute() throws Exception {
        mockMvc.perform(get(ROUTE_URI + "?source=v8&dest=v0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("V8"))
                .andExpect(jsonPath("$.dest").value("V0"))
                .andExpect(jsonPath("$.cost").value("14.0"))
                .andExpect(jsonPath("$.connections.[0]").value("V8"))
                .andExpect(jsonPath("$.connections.[1]").value("V2"))
                .andExpect(jsonPath("$.connections.[2]").value("V1"))
                .andExpect(jsonPath("$.connections.[3]").value("V0"));
    }
}
