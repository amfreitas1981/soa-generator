package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WsdlRequestTest {
    @Test
    void testConstructorAndGetters() {
        // Arrange
        String serviceName = "TestService";
        List<FieldDefinition> inputFields = Collections.singletonList(new FieldDefinition());
        List<FieldDefinition> outputFields = Collections.singletonList(new FieldDefinition());

        // Act
        WsdlRequest request = new WsdlRequest(serviceName, inputFields, outputFields);

        // Assert
        assertEquals(serviceName, request.getServiceName());
        assertEquals(inputFields, request.getInputFields());
        assertEquals(outputFields, request.getOutputFields());
    }

    @Test
    void testSetters() {
        // Arrange
        WsdlRequest request = new WsdlRequest();
        String serviceName = "UpdatedService";
        List<FieldDefinition> inputFields = Collections.singletonList(new FieldDefinition());
        List<FieldDefinition> outputFields = Collections.singletonList(new FieldDefinition());

        // Act
        request.setServiceName(serviceName);
        request.setInputFields(inputFields);
        request.setOutputFields(outputFields);

        // Assert
        assertEquals(serviceName, request.getServiceName());
        assertEquals(inputFields, request.getInputFields());
        assertEquals(outputFields, request.getOutputFields());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        WsdlRequest request = new WsdlRequest();

        // Assert
        assertNull(request.getServiceName());
        assertNull(request.getInputFields());
        assertNull(request.getOutputFields());
    }
}