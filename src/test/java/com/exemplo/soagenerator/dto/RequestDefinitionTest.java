package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestDefinitionTest {

    @Test
    void testRequestDefinition_SettersAndGetters() {
        RequestDefinition request = new RequestDefinition();

        ParameterDefinition param = new ParameterDefinition();
        param.setName("id");
        param.setType("integer");
        param.setRequired(true);

        FieldDefinition field = new FieldDefinition();
        field.setName("username");
        field.setType("string");

        request.setParameters(List.of(param));
        request.setFields(List.of(field));

        assertEquals(1, request.getParameters().size());
        assertEquals("id", request.getParameters().get(0).getName());
        assertEquals("integer", request.getParameters().get(0).getType());
        assertTrue(request.getParameters().get(0).isRequired());

        assertEquals(1, request.getFields().size());
        assertEquals("username", request.getFields().get(0).getName());
        assertEquals("string", request.getFields().get(0).getType());
    }
}
