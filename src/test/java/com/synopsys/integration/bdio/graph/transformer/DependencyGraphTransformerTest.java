package com.synopsys.integration.bdio.graph.transformer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.synopsys.integration.bdio.BdioWriter;
import com.synopsys.integration.bdio.SimpleBdioFactory;
import com.synopsys.integration.bdio.graph.ProjectDependencyGraph;
import com.synopsys.integration.bdio.model.BdioId;
import com.synopsys.integration.bdio.model.SimpleBdioDocument;
import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.utility.JsonTestUtils;

class DependencyGraphTransformerTest {
    private final JsonTestUtils jsonTestUtils = new JsonTestUtils();

    @Test
    void testTransformingDocument() throws URISyntaxException, IOException, JSONException {
        Dependency projectDependency = Dependency.FACTORY.createMavenDependency("projectGroup", "projectName", "projectVersion");
        ProjectDependencyGraph dependencyGraph = new ProjectDependencyGraph(projectDependency);

        ExternalId childExternalId = ExternalId.FACTORY.createMavenExternalId("componentGroup1", "componentArtifact1", "1.0.0");
        Dependency child = new Dependency("componentArtifact1", "1.0.0", childExternalId);
        dependencyGraph.addChildrenToRoot(child);

        ExternalId transitiveExternalId = ExternalId.FACTORY.createMavenExternalId("transitiveGroup", "transitiveArtifact", "2.1.0");
        Dependency transitive = new Dependency("transitiveArtifact", "2.1.0", transitiveExternalId);
        dependencyGraph.addParentWithChild(child, transitive);

        SimpleBdioFactory simpleBdioFactory = new SimpleBdioFactory();
        SimpleBdioDocument simpleBdioDocument = simpleBdioFactory.createPopulatedBdioDocument(dependencyGraph);

        // we are overriding the default value of a new creation info just to pass the json comparison
        simpleBdioDocument.getBillOfMaterials().creationInfo = null;

        // we are overriding the default value of a new uuid just to pass the json comparison
        simpleBdioDocument.getBillOfMaterials().id = BdioId.createFromUUID("123");

        String expectedJson = jsonTestUtils.getExpectedJson("transformer.jsonld");

        Writer writer = new StringWriter();
        try (BdioWriter bdioWriter = new BdioWriter(new Gson(), writer)) {
            bdioWriter.writeSimpleBdioDocument(simpleBdioDocument);
        }

        String actualJson = writer.toString();
        jsonTestUtils.verifyJsonArraysEqual(expectedJson, actualJson);
    }

}
