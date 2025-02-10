package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WadlRequestTest {

    @Test
    void testWadlRequestGettersAndSetters() {
        WadlRequest request = new WadlRequest();

        request.setServiceName("TestService");
        request.setBaseUri("http://example.com/api");
        request.setMethods(List.of(new WadlMethod()));

        assertEquals("TestService", request.getServiceName());
        assertEquals("http://example.com/api", request.getBaseUri());
        assertNotNull(request.getMethods());
        assertEquals(1, request.getMethods().size());
    }
}
