package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WadlMethodTest {

    @Test
    void testWadlMethodSettersAndGetters() {
        WadlMethod method = new WadlMethod();

        String name = "GET";
        String path = "/example";
        String description = "Example method";
        WadlRequestDetails request = new WadlRequestDetails();
        WadlResponseDetails response = new WadlResponseDetails();

        method.setName(name);
        method.setPath(path);
        method.setDescription(description);
        method.setRequest(request);
        method.setResponse(response);

        assertEquals(name, method.getName());
        assertEquals(path, method.getPath());
        assertEquals(description, method.getDescription());
        assertEquals(request, method.getRequest());
        assertEquals(response, method.getResponse());
    }
}
