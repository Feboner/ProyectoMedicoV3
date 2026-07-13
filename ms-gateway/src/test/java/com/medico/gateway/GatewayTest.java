package com.medico.gateway;

import com.medico.gateway.dto.GatewayRequest;
import com.medico.gateway.dto.GatewayResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GatewayTest {

    @Test
    void mapearSolicitudGateway() {
        GatewayRequest request = new GatewayRequest("/api/productos", "GET");
        GatewayResponse response = new GatewayResponse("OK", request.getPath());

        assertEquals("OK", response.getStatus());
        assertEquals("/api/productos", response.getRoute());
    }

    @Test
    void conservarMetodoHttp() {
        GatewayRequest request = new GatewayRequest("/api/auth", "POST");
        assertEquals("POST", request.getMethod());
    }
}
