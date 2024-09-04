/*
 * integration-bdio
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.blackduck.integration.bdio.model.externalid.ExternalId;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.blackduck.integration.bdio.model.BdioBillOfMaterials;
import com.blackduck.integration.bdio.model.BdioComponent;
import com.blackduck.integration.bdio.model.BdioCreationInfo;
import com.blackduck.integration.bdio.model.BdioExternalIdentifier;
import com.blackduck.integration.bdio.model.BdioId;
import com.blackduck.integration.bdio.model.BdioProject;
import com.blackduck.integration.bdio.model.SpdxCreator;

public class BdioNodeFactory {
    public static final String UNKNOWN_LIBRARY_VERSION = "UnknownVersion";

    private static final String VERSION_RESOURCE_PATH = "com/blackduck/integration/bdio/version.txt";

    private final BdioPropertyHelper bdioPropertyHelper;

    public BdioNodeFactory(BdioPropertyHelper bdioPropertyHelper) {
        this.bdioPropertyHelper = bdioPropertyHelper;
    }

    public BdioBillOfMaterials createBillOfMaterials(String projectName, String projectVersion) {
        String codeLocationName = String.format("%s/%s Black Duck I/O Export", projectName, projectVersion);

        return createBillOfMaterials(codeLocationName, projectName, projectVersion);
    }

    public BdioBillOfMaterials createBillOfMaterials(@Nullable String codeLocationName, String projectName, String projectVersion) {
        BdioBillOfMaterials billOfMaterials = new BdioBillOfMaterials();
        billOfMaterials.id = BdioId.createFromUUID(UUID.randomUUID().toString());
        if (StringUtils.isNotBlank(codeLocationName)) {
            billOfMaterials.spdxName = codeLocationName;
        } else {
            billOfMaterials.spdxName = String.format("%s/%s Black Duck I/O Export", projectName, projectVersion);
        }
        billOfMaterials.bdioSpecificationVersion = "1.1.0";

        billOfMaterials.creationInfo = new BdioCreationInfo();
        billOfMaterials.creationInfo.created = Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        String version = BdioNodeFactory.UNKNOWN_LIBRARY_VERSION;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BdioNodeFactory.VERSION_RESOURCE_PATH)) {
            if (inputStream != null) {
                version = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {
            // Library version is not critical to BdioBillOfMaterials creation.
        }
        billOfMaterials.creationInfo.addSpdxCreator(SpdxCreator.createToolSpdxCreator("IntegrationBdio", version));

        return billOfMaterials;
    }

    public BdioProject createProject(String projectName, String projectVersion, BdioId bdioId, ExternalId externalId) {
        BdioExternalIdentifier externalIdentifier = bdioPropertyHelper.createExternalIdentifier(externalId);
        return createProject(projectName, projectVersion, bdioId, externalIdentifier);
    }

    public BdioProject createProject(String projectName, String projectVersion, BdioId bdioId, BdioExternalIdentifier externalIdentifier) {
        BdioProject project = createProject(projectName, projectVersion, bdioId);
        project.bdioExternalIdentifier = externalIdentifier;

        return project;
    }

    public BdioProject createProject(String projectName, String projectVersion) {
        return createProject(projectName, projectVersion, BdioId.createFromPieces(projectName, projectVersion));
    }

    public BdioProject createProject(String projectName, String projectVersion, BdioId bdioId) {
        BdioProject project = new BdioProject();
        project.id = bdioId;
        project.name = projectName;
        project.version = projectVersion;

        return project;
    }

    public BdioComponent createComponent(String componentName, String componentVersion, ExternalId externalId) {
        BdioExternalIdentifier externalIdentifier = bdioPropertyHelper.createExternalIdentifier(externalId);
        return createComponent(componentName, componentVersion, externalId.createBdioId(), externalIdentifier);
    }

    public BdioComponent createComponent(String componentName, String componentVersion, BdioId bdioId, BdioExternalIdentifier externalIdentifier) {
        BdioComponent component = new BdioComponent();
        component.id = bdioId;
        component.name = componentName;
        component.version = componentVersion;
        component.bdioExternalIdentifier = externalIdentifier;

        return component;
    }

}
