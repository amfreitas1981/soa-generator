package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodDefinitionTest {

    @Test
    void testMethodDefinition_SettersAndGetters() {
        MethodDefinition method = new MethodDefinition();
        method.setName("getUser");
        method.setPath("/users/{id}");
        method.setDescription("Retrieve user details");

        RequestDefinition request = new RequestDefinition();
        ResponseDefinition response = new ResponseDefinition();

        method.setRequest(request);
        method.setResponse(response);

        assertEquals("getUser", method.getName());
        assertEquals("/users/{id}", method.getPath());
        assertEquals("Retrieve user details", method.getDescription());
        assertEquals(request, method.getRequest());
        assertEquals(response, method.getResponse());
    }
}
