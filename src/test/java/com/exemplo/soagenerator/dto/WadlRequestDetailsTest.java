package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WadlRequestDetailsTest {
    @Test
    void testGettersAndSetters() {
        WadlRequestDetails requestDetails = new WadlRequestDetails();

        WadlParameter param = new WadlParameter();
        param.setName("id");
        param.setType("string");
        param.setRequired(true);

        WadlField field = new WadlField();
        field.setName("name");
        field.setType("string");

        requestDetails.setParameters(List.of(param));
        requestDetails.setFields(List.of(field));

        assertNotNull(requestDetails.getParameters());
        assertEquals(1, requestDetails.getParameters().size());
        assertEquals("id", requestDetails.getParameters().get(0).getName());
        assertEquals("string", requestDetails.getParameters().get(0).getType());
        assertTrue(requestDetails.getParameters().get(0).isRequired());

        assertNotNull(requestDetails.getFields());
        assertEquals(1, requestDetails.getFields().size());
        assertEquals("name", requestDetails.getFields().get(0).getName());
        assertEquals("string", requestDetails.getFields().get(0).getType());
    }
}
