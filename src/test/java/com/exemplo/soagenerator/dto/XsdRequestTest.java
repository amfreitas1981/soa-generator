package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XsdRequestTest {

    @Test
    void testXsdRequest_SettersAndGetters() {
        XsdField inputField = new XsdField("id", "integer", true, false, false);
        XsdField outputField = new XsdField("name", "string", false, false, false);
        XsdObject complexObject = new XsdObject("User", List.of(new XsdField("email", "string", true, false, false)));

        XsdRequest xsdRequest = new XsdRequest();
        xsdRequest.setServiceName("UserService");
        xsdRequest.setInputFields(List.of(inputField));
        xsdRequest.setOutputFields(List.of(outputField));
        xsdRequest.setObjects(List.of(complexObject));

        assertEquals("UserService", xsdRequest.getServiceName());
        assertEquals(1, xsdRequest.getInputFields().size());
        assertEquals("id", xsdRequest.getInputFields().get(0).getName());
        assertEquals("integer", xsdRequest.getInputFields().get(0).getType());

        assertEquals(1, xsdRequest.getOutputFields().size());
        assertEquals("name", xsdRequest.getOutputFields().get(0).getName());
        assertEquals("string", xsdRequest.getOutputFields().get(0).getType());

        assertEquals(1, xsdRequest.getObjects().size());
        assertEquals("User", xsdRequest.getObjects().get(0).getName());
        assertEquals(1, xsdRequest.getObjects().get(0).getFields().size());
        assertEquals("email", xsdRequest.getObjects().get(0).getFields().get(0).getName());
    }

    @Test
    void testXsdRequest_ParameterizedConstructor() {
        XsdField inputField = new XsdField("id", "integer", true, false, false);
        XsdField outputField = new XsdField("name", "string", false, false, false);
        XsdObject complexObject = new XsdObject("User", List.of(new XsdField("email", "string", true, false, false)));

        XsdRequest xsdRequest = new XsdRequest("UserService", List.of(inputField), List.of(outputField), List.of(complexObject));

        assertEquals("UserService", xsdRequest.getServiceName());
        assertEquals(1, xsdRequest.getInputFields().size());
        assertEquals("id", xsdRequest.getInputFields().get(0).getName());
        assertEquals("integer", xsdRequest.getInputFields().get(0).getType());

        assertEquals(1, xsdRequest.getOutputFields().size());
        assertEquals("name", xsdRequest.getOutputFields().get(0).getName());
        assertEquals("string", xsdRequest.getOutputFields().get(0).getType());

        assertEquals(1, xsdRequest.getObjects().size());
        assertEquals("User", xsdRequest.getObjects().get(0).getName());
        assertEquals(1, xsdRequest.getObjects().get(0).getFields().size());
        assertEquals("email", xsdRequest.getObjects().get(0).getFields().get(0).getName());
    }
}
