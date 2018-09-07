/**
 * Integration Bdio
 * <p>
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.hub.bdio.simple;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.json.JSONException;
import org.junit.Test;

import com.google.gson.Gson;
import com.synopsys.integration.hub.bdio.BdioReader;
import com.synopsys.integration.hub.bdio.BdioTransformer;
import com.synopsys.integration.hub.bdio.SimpleBdioFactory;
import com.synopsys.integration.hub.bdio.graph.DependencyGraph;
import com.synopsys.integration.hub.bdio.model.BdioComponent;
import com.synopsys.integration.hub.bdio.model.BdioRelationship;
import com.synopsys.integration.hub.bdio.model.Forge;
import com.synopsys.integration.hub.bdio.model.SimpleBdioDocument;
import com.synopsys.integration.hub.bdio.model.externalid.ExternalId;
import com.synopsys.integration.hub.bdio.utility.JsonTestUtils;

public class BdioTransformerTest {
    private final JsonTestUtils jsonTestUtils = new JsonTestUtils();

    @Test
    public void testTransformingDependencyGraphSample() throws URISyntaxException, IOException, JSONException {
        testTransformingDependencyGraphs("sample.jsonld", true);
    }

    @Test
    public void testTransformingDependencyGraphSampleEdge() throws URISyntaxException, IOException, JSONException {
        // this is testing re-creating a simple bdio document when the externalIdMetadata fields are not populated
        // in practice, this should not happen with files created with this library, but may happen with older versions, or other bdio files.
        testTransformingDependencyGraphs("sample-edge.jsonld", false);
    }

    public void testTransformingDependencyGraphs(final String filename, final boolean testEqualityOfMetadata) throws URISyntaxException, IOException, JSONException {
        final String expectedJson = jsonTestUtils.getExpectedJson(filename);

        final Reader reader = new StringReader(expectedJson);
        SimpleBdioDocument doc = null;
        try (BdioReader bdioReader = new BdioReader(new Gson(), reader)) {
            doc = bdioReader.readSimpleBdioDocument();
        }

        final Map<String, Forge> forgeMap = new HashMap<>();
        forgeMap.put("maven", Forge.MAVEN);
        final BdioTransformer transformer = new BdioTransformer(forgeMap);

        final DependencyGraph graph = transformer.transformToDependencyGraph(doc.project, doc.components);

        assertEquals(1, graph.getRootDependencies().size());

        final ExternalId projectId = new ExternalId(Forge.MAVEN);
        projectId.group = "com.blackducksoftware.gradle.test";
        projectId.name = "gradleTestProject";
        projectId.version = "99.5-SNAPSHOT";

        final SimpleBdioFactory simpleBdioFactory = new SimpleBdioFactory();
        final SimpleBdioDocument simpleBdioDocument = simpleBdioFactory.createSimpleBdioDocument(doc.project.name, doc.project.version, projectId, graph);

        simpleBdioDocument.billOfMaterials.id = doc.billOfMaterials.id;
        simpleBdioDocument.billOfMaterials.creationInfo = doc.billOfMaterials.creationInfo;

        assertEquals(true, EqualsBuilder.reflectionEquals(simpleBdioDocument.billOfMaterials, doc.billOfMaterials));
        assertEquals(true, EqualsBuilder.reflectionEquals(simpleBdioDocument.project, doc.project, "bdioExternalIdentifier", "relationships"));
        if (!testEqualityOfMetadata) {
            simpleBdioDocument.project.bdioExternalIdentifier.externalIdMetaData = null;
        }
        assertEquals(true, EqualsBuilder.reflectionEquals(simpleBdioDocument.project.bdioExternalIdentifier, doc.project.bdioExternalIdentifier));
        assertRelationships(doc.project.relationships, simpleBdioDocument.project.relationships);

        assertEquals(doc.components.size(), simpleBdioDocument.components.size());
        for (final BdioComponent expected : simpleBdioDocument.components) {
            boolean fnd = false;
            for (final BdioComponent actual : doc.components) {
                if (expected.id.equals(actual.id)) {
                    assertEquals(false, fnd);
                    fnd = true;

                    assertEquals(true, EqualsBuilder.reflectionEquals(expected, actual, "bdioExternalIdentifier", "relationships"));
                    if (!testEqualityOfMetadata) {
                        expected.bdioExternalIdentifier.externalIdMetaData = null;
                    }
                    assertEquals(true, EqualsBuilder.reflectionEquals(expected.bdioExternalIdentifier, actual.bdioExternalIdentifier, "externalIdMetaData"));
                    assertRelationships(expected.relationships, actual.relationships);

                }
            }
            assertEquals(expected.id, true, fnd);
        }

    }

    private void assertRelationships(final List<BdioRelationship> expectedList, final List<BdioRelationship> actualList) {
        assertEquals(expectedList.size(), actualList.size());
        for (final BdioRelationship expected : expectedList) {
            boolean fnd = false;
            for (final BdioRelationship actual : actualList) {
                if (expected.related.equals(actual.related)) {
                    assertEquals(false, fnd);
                    fnd = true;

                    assertEquals(true, EqualsBuilder.reflectionEquals(expected, actual));
                }
            }
            assertEquals(expected.related, true, fnd);
        }
    }

}