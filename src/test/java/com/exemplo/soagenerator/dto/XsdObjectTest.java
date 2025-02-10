package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class XsdObjectTest {
    @Test
    void testXsdObjectConstructorAndGetters() {
        // Arrange
        List<XsdField> fields = Arrays.asList(
                new XsdField("field1", "string", true, false, false),
                new XsdField("field2", "int", false, false, false)
        );

        // Act
        XsdObject object = new XsdObject("TestObject", fields);

        // Assert
        assertEquals("TestObject", object.getName());
        assertEquals(2, object.getFields().size());
        assertEquals("field1", object.getFields().get(0).getName());
        assertEquals("field2", object.getFields().get(1).getName());
    }

    @Test
    void testXsdObjectSetters() {
        // Arrange
        XsdObject object = new XsdObject();
        List<XsdField> fields = Arrays.asList(
                new XsdField("field1", "string", true, false, false)
        );

        // Act
        object.setName("UpdatedObject");
        object.setFields(fields);

        // Assert
        assertEquals("UpdatedObject", object.getName());
        assertEquals(1, object.getFields().size());
        assertEquals("field1", object.getFields().get(0).getName());
    }
}
