/**
 * Integration Bdio
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.bdio.simple;

import java.util.UUID;

import com.blackducksoftware.integration.hub.bdio.simple.model.BdioBillOfMaterials;
import com.blackducksoftware.integration.hub.bdio.simple.model.BdioComponent;
import com.blackducksoftware.integration.hub.bdio.simple.model.BdioExternalIdentifier;
import com.blackducksoftware.integration.hub.bdio.simple.model.BdioProject;

public class BdioNodeFactory {
    private final BdioPropertyHelper bdioPropertyHelper;

    public BdioNodeFactory(final BdioPropertyHelper bdioPropertyHelper) {
        this.bdioPropertyHelper = bdioPropertyHelper;
    }

    public BdioBillOfMaterials createBillOfMaterials(final String projectName) {
        final BdioBillOfMaterials billOfMaterials = new BdioBillOfMaterials();
        billOfMaterials.id = String.format("uuid:%s", UUID.randomUUID());
        billOfMaterials.name = String.format("%s Black Duck I/O Export", projectName);
        billOfMaterials.bdioSpecificationVersion = "1.1.0";

        return billOfMaterials;
    }

    public BdioProject createProject(final String projectName, final String projectVersion, final String bdioId, final String forge,
            final String externalId) {
        final BdioExternalIdentifier externalIdentifier = bdioPropertyHelper.createExternalIdentifier(forge, externalId);

        return createProject(projectName, projectVersion, bdioId, externalIdentifier);
    }

    public BdioProject createProject(final String projectName, final String projectVersion, final String bdioId,
            final BdioExternalIdentifier externalIdentifier) {
        final BdioProject project = new BdioProject();
        project.id = bdioId;
        project.name = projectName;
        project.version = projectVersion;
        project.bdioExternalIdentifier = externalIdentifier;

        return project;
    }

    public BdioComponent createComponent(final String componentName, final String componentVersion, final String bdioId, final String forge,
            final String externalId) {
        final BdioExternalIdentifier externalIdentifier = bdioPropertyHelper.createExternalIdentifier(forge, externalId);

        return createComponent(componentName, componentVersion, bdioId, externalIdentifier);
    }

    public BdioComponent createComponent(final String componentName, final String componentVersion, final String bdioId,
            final BdioExternalIdentifier externalIdentifier) {
        final BdioComponent component = new BdioComponent();
        component.id = bdioId;
        component.name = componentName;
        component.version = componentVersion;
        component.bdioExternalIdentifier = externalIdentifier;

        return component;
    }

}
