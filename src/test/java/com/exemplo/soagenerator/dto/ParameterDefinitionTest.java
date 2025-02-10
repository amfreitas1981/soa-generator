package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterDefinitionTest {

    @Test
    void testParameterDefinition_SettersAndGetters() {
        ParameterDefinition param = new ParameterDefinition();
        param.setName("id");
        param.setType("integer");
        param.setRequired(true);

        assertEquals("id", param.getName());
        assertEquals("integer", param.getType());
        assertTrue(param.isRequired());
    }
}
