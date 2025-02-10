package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WadlParameterTest {

    @Test
    void testWadlParameter() {
        WadlParameter parameter = new WadlParameter();

        parameter.setName("id");
        parameter.setType("xs:int");
        parameter.setRequired(true);

        assertEquals("id", parameter.getName());
        assertEquals("xs:int", parameter.getType());
        assertTrue(parameter.isRequired());
    }
}
