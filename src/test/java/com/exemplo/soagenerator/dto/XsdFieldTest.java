package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XsdFieldTest {
    @Test
    void testXsdFieldConstructorAndGetters() {
        // Arrange & Act
        XsdField field = new XsdField("testField", "string", true, false, false);

        // Assert
        assertEquals("testField", field.getName());
        assertEquals("string", field.getType());
        assertTrue(field.isRequired());
        assertFalse(field.isObject());
        assertFalse(field.isList());
    }

    @Test
    void testXsdFieldSetters() {
        // Arrange
        XsdField field = new XsdField();

        // Act
        field.setName("updatedField");
        field.setType("int");
        field.setRequired(true);
        field.setObject(true);
        field.setList(true);

        // Assert
        assertEquals("updatedField", field.getName());
        assertEquals("int", field.getType());
        assertTrue(field.isRequired());
        assertTrue(field.isObject());
        assertTrue(field.isList());
    }
}
