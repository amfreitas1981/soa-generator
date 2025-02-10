package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseDefinitionTest {

    @Test
    void testResponseDefinition_SettersAndGetters() {
        ResponseDefinition response = new ResponseDefinition();

        FieldDefinition field = new FieldDefinition();
        field.setName("userId");
        field.setType("integer");

        response.setFields(List.of(field));

        assertEquals(1, response.getFields().size());
        assertEquals("userId", response.getFields().get(0).getName());
        assertEquals("integer", response.getFields().get(0).getType());
    }
}
