package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldDefinitionTest {
    @Test
    void testSettersAndGetters() {
        // Arrange
        FieldDefinition field = new FieldDefinition();
        String name = "testField";
        String type = "string";
        List<FieldDefinition> subFields = Collections.singletonList(new FieldDefinition());

        // Act
        field.setName(name);
        field.setType(type);
        field.setRequired(true);
        field.setObject(true);
        field.setList(true);
        field.setFields(subFields);

        // Assert
        assertEquals(name, field.getName());
        assertEquals(type, field.getType());
        assertTrue(field.isRequired());
        assertTrue(field.isObject());
        assertTrue(field.isList());
        assertEquals(subFields, field.getFields());
    }

    @Test
    void testDefaultValues() {
        // Act
        FieldDefinition field = new FieldDefinition();

        // Assert
        assertNull(field.getName());
        assertNull(field.getType());
        assertFalse(field.isRequired());
        assertFalse(field.isObject());
        assertFalse(field.isList());
        assertNull(field.getFields());
    }

    @Test
    void testNestedFields() {
        // Arrange
        FieldDefinition parentField = new FieldDefinition();
        FieldDefinition childField = new FieldDefinition();

        childField.setName("childField");
        childField.setType("string");

        // Act
        parentField.setName("parentField");
        parentField.setType("object");
        parentField.setObject(true);
        parentField.setFields(Collections.singletonList(childField));

        // Assert
        assertEquals("parentField", parentField.getName());
        assertTrue(parentField.isObject());
        assertEquals(1, parentField.getFields().size());
        assertEquals("childField", parentField.getFields().get(0).getName());
    }
}
