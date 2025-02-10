package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WadlFieldTest {

    @Test
    void testWadlField_SettersAndGetters() {
        WadlField field = new WadlField();
        field.setName("campoTeste");
        field.setType("string");

        assertEquals("campoTeste", field.getName());
        assertEquals("string", field.getType());
    }

    @Test
    void testWadlField_DefaultConstructor() {
        WadlField field = new WadlField();
        assertNull(field.getName());
        assertNull(field.getType());
    }
}
