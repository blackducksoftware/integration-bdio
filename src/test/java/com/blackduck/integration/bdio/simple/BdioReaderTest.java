package com.blackduck.integration.bdio.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import com.blackduck.integration.bdio.BdioReader;
import com.blackduck.integration.bdio.model.BdioId;
import com.blackduck.integration.bdio.model.SimpleBdioDocument;
import com.blackduck.integration.bdio.utility.JsonTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.blackduck.integration.bdio.model.BdioComponent;

public class BdioReaderTest {
    private final JsonTestUtils jsonTestUtils = new JsonTestUtils();

    @Test
    public void testReaderOnSample() throws IOException, URISyntaxException {
        String expectedJson = jsonTestUtils.getExpectedJson("sample.jsonld");

        InputStream reader = new ByteArrayInputStream(expectedJson.getBytes(StandardCharsets.UTF_8.name()));
        SimpleBdioDocument doc;
        try (BdioReader bdioReader = new BdioReader(new Gson(), reader)) {
            doc = bdioReader.readSimpleBdioDocument();
        }

        assertNotNull(doc);

        assertNotNull(doc.getBillOfMaterials());
        assertNotNull(doc.getBillOfMaterials().relationships);
        assertEquals("1.1.0", doc.getBillOfMaterials().bdioSpecificationVersion);
        assertEquals(BdioId.createFromUUID("45772d33-5353-44f1-8681-3d8a15540646"), doc.getBillOfMaterials().id);
        assertEquals("BillOfMaterials", doc.getBillOfMaterials().type);
        assertTrue(doc.getBillOfMaterials().creationInfo.getCreator().contains("Tool: integration-bdio-test-0.0.1-SNAPSHOT"));
        assertEquals("gradleTestProject/99.5-SNAPSHOT Black Duck I/O Export", doc.getBillOfMaterials().spdxName);
        assertEquals(0, doc.getBillOfMaterials().relationships.size());

        assertNotNull(doc.getProject());
        assertNotNull(doc.getProject().bdioExternalIdentifier);
        assertNotNull(doc.getProject().relationships);
        assertEquals("99.5-SNAPSHOT", doc.getProject().version);
        assertEquals(new BdioId("http:maven/com.blackducksoftware.gradle.test/gradleTestProject/99.5-SNAPSHOT"), doc.getProject().id);
        assertEquals("Project", doc.getProject().type);
        assertEquals("gradleTestProject", doc.getProject().name);
        assertEquals("maven", doc.getProject().bdioExternalIdentifier.forge);
        assertEquals("com.blackducksoftware.gradle.test:gradleTestProject:99.5-SNAPSHOT", doc.getProject().bdioExternalIdentifier.externalId);

        assertEquals(1, doc.getProject().relationships.size());
        assertEquals(new BdioId("http:maven/org.apache.cxf/cxf-bundle/2.7.7"), doc.getProject().relationships.get(0).related);
        assertEquals("DYNAMIC_LINK", doc.getProject().relationships.get(0).relationshipType);

        assertNotNull(doc.getComponents());
        assertEquals(4, doc.getComponents().size());

        BdioComponent first = doc.getComponents().get(0);

        assertNotNull(first);
        assertNotNull(first.bdioExternalIdentifier);
        assertNotNull(first.relationships);

        assertEquals("cxf-bundle", first.name);
        assertEquals("2.7.7", first.version);
        assertEquals(new BdioId("http:maven/org.apache.cxf/cxf-bundle/2.7.7"), first.id);
        assertEquals("Component", first.type);

        assertEquals("maven", first.bdioExternalIdentifier.forge);
        assertEquals("org.apache.cxf:cxf-bundle:2.7.7", first.bdioExternalIdentifier.externalId);

        assertEquals(2, first.relationships.size());
        assertEquals(new BdioId("http:maven/org.apache.velocity/velocity/1.7"), first.relationships.get(0).related);
        assertEquals("DYNAMIC_LINK", first.relationships.get(0).relationshipType);
        assertEquals(new BdioId("http:maven/commons-lang/commons-lang/2.6"), first.relationships.get(1).related);
        assertEquals("DYNAMIC_LINK", first.relationships.get(1).relationshipType);
    }

}
