package com.exemplo.soagenerator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.ArrayList;

class WadlResponseDetailsTest {

    @Test
    void testWadlResponseDetails_SettersAndGetters() {
        WadlResponseDetails responseDetails = new WadlResponseDetails();

        List<WadlField> fields = new ArrayList<>();
        WadlField field = new WadlField();
        field.setName("status");
        field.setType("string");
        fields.add(field);

        responseDetails.setFields(fields);

        assertNotNull(responseDetails.getFields());
        assertEquals(1, responseDetails.getFields().size());
        assertEquals("status", responseDetails.getFields().get(0).getName());
        assertEquals("string", responseDetails.getFields().get(0).getType());
    }
}
