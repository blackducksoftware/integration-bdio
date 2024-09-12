/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleBdioDocument {
    private BdioBillOfMaterials billOfMaterials;
    private BdioProject project;
    private List<BdioComponent> components = new ArrayList<>();

    public BdioBillOfMaterials getBillOfMaterials() {
        return billOfMaterials;
    }

    public void setBillOfMaterials(BdioBillOfMaterials billOfMaterials) {
        this.billOfMaterials = billOfMaterials;
    }

    public BdioProject getProject() {
        return project;
    }

    public void setProject(BdioProject project) {
        this.project = project;
    }

    public List<BdioComponent> getComponents() {
        return components;
    }

    public void setComponents(List<BdioComponent> components) {
        this.components = components;
    }
}