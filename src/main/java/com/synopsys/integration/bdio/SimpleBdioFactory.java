/*
 * integration-bdio
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.bdio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.bdio.graph.DependencyGraphTransformer;
import com.synopsys.integration.bdio.graph.ProjectDependencyGraph;
import com.synopsys.integration.bdio.model.BdioBillOfMaterials;
import com.synopsys.integration.bdio.model.BdioComponent;
import com.synopsys.integration.bdio.model.BdioId;
import com.synopsys.integration.bdio.model.BdioNode;
import com.synopsys.integration.bdio.model.BdioProject;
import com.synopsys.integration.bdio.model.SimpleBdioDocument;
import com.synopsys.integration.bdio.model.dependency.DependencyFactory;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;

public class SimpleBdioFactory {
    private final BdioPropertyHelper bdioPropertyHelper;
    private final BdioNodeFactory bdioNodeFactory;
    private final DependencyGraphTransformer dependencyGraphTransformer;
    private final ExternalIdFactory externalIdFactory;
    private final DependencyFactory dependencyFactory;
    private final Gson gson;

    public SimpleBdioFactory() {
        bdioPropertyHelper = new BdioPropertyHelper();
        bdioNodeFactory = new BdioNodeFactory(bdioPropertyHelper);
        dependencyGraphTransformer = new DependencyGraphTransformer(bdioPropertyHelper, bdioNodeFactory);
        externalIdFactory = new ExternalIdFactory();
        dependencyFactory = new DependencyFactory(externalIdFactory);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public SimpleBdioFactory(
        BdioPropertyHelper bdioPropertyHelper,
        BdioNodeFactory bdioNodeFactory,
        DependencyGraphTransformer dependencyGraphTransformer,
        ExternalIdFactory externalIdFactory,
        DependencyFactory dependencyFactory,
        Gson gson
    ) {
        this.bdioPropertyHelper = bdioPropertyHelper;
        this.bdioNodeFactory = bdioNodeFactory;
        this.dependencyGraphTransformer = dependencyGraphTransformer;
        this.externalIdFactory = externalIdFactory;
        this.dependencyFactory = dependencyFactory;
        this.gson = gson;
    }

    public BdioWriter createBdioWriter(Writer writer) throws IOException {
        return new BdioWriter(gson, writer);
    }

    public BdioWriter createBdioWriter(OutputStream outputStream) throws IOException {
        return new BdioWriter(gson, outputStream);
    }

    public void writeSimpleBdioDocument(BdioWriter bdioWriter, SimpleBdioDocument simpleBdioDocument) {
        bdioWriter.writeSimpleBdioDocument(simpleBdioDocument);
    }

    public void writeSimpleBdioDocumentToFile(File bdioFile, SimpleBdioDocument simpleBdioDocument) throws IOException {
        try (BdioWriter bdioWriter = createBdioWriter(new FileOutputStream(bdioFile))) {
            writeSimpleBdioDocument(bdioWriter, simpleBdioDocument);
        }
    }

    public SimpleBdioDocument createPopulatedBdioDocument(ProjectDependencyGraph projectDependencyGraph) {
        return createPopulatedBdioDocument(null, projectDependencyGraph);
    }

    public SimpleBdioDocument createPopulatedBdioDocument(@Nullable String codeLocationName, ProjectDependencyGraph projectDependencyGraph) {
        SimpleBdioDocument simpleBdioDocument = createEmptyBdioDocument(codeLocationName, projectDependencyGraph.getProjectDependency().getExternalId());
        populateComponents(simpleBdioDocument, projectDependencyGraph);
        return simpleBdioDocument;
    }

    public SimpleBdioDocument createEmptyBdioDocument(@Nullable String codeLocationName, ExternalId projectExternalId) {
        BdioId projectId = projectExternalId.createBdioId();
        BdioProject project = bdioNodeFactory.createProject(projectExternalId.getName(), projectExternalId.getVersion(), projectId);

        project.bdioExternalIdentifier = bdioPropertyHelper.createExternalIdentifier(projectExternalId);

        return createEmptyBdioDocument(codeLocationName, project);
    }

    public SimpleBdioDocument createEmptyBdioDocument(@Nullable String codeLocationName, String projectName, String projectVersionName) {
        BdioProject project = bdioNodeFactory.createProject(projectName, projectVersionName);
        return createEmptyBdioDocument(codeLocationName, project);
    }

    public SimpleBdioDocument createEmptyBdioDocument(@Nullable String codeLocationName, BdioProject project) {
        BdioBillOfMaterials billOfMaterials = bdioNodeFactory.createBillOfMaterials(codeLocationName, project.name, project.version);

        SimpleBdioDocument simpleBdioDocument = new SimpleBdioDocument();
        simpleBdioDocument.setBillOfMaterials(billOfMaterials);
        simpleBdioDocument.setProject(project);

        return simpleBdioDocument;
    }

    public void populateComponents(SimpleBdioDocument simpleBdioDocument, ProjectDependencyGraph projectDependencyGraph) {
        Map<ExternalId, BdioNode> existingComponents = new HashMap<>();
        existingComponents.put(projectDependencyGraph.getProjectDependency().getExternalId(), simpleBdioDocument.getProject());

        List<BdioComponent> bdioComponents = dependencyGraphTransformer.transformDependencyGraph(
            projectDependencyGraph,
            simpleBdioDocument.getProject(),
            projectDependencyGraph.getDirectDependencies(),
            existingComponents
        );
        simpleBdioDocument.setComponents(bdioComponents);
    }

    public BdioPropertyHelper getBdioPropertyHelper() {
        return bdioPropertyHelper;
    }

    public BdioNodeFactory getBdioNodeFactory() {
        return bdioNodeFactory;
    }

    public DependencyGraphTransformer getDependencyGraphTransformer() {
        return dependencyGraphTransformer;
    }

    public ExternalIdFactory getExternalIdFactory() {
        return externalIdFactory;
    }

    public DependencyFactory getDependencyFactory() {
        return dependencyFactory;
    }

}
