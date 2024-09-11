/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blackduck.integration.bdio.BdioNodeFactory;
import com.blackduck.integration.bdio.BdioPropertyHelper;
import com.blackduck.integration.bdio.model.BdioComponent;
import com.blackduck.integration.bdio.model.BdioExternalIdentifier;
import com.blackduck.integration.bdio.model.BdioId;
import com.blackduck.integration.bdio.model.BdioNode;
import com.blackduck.integration.bdio.model.dependency.Dependency;
import com.blackduck.integration.bdio.model.externalid.ExternalId;

public class DependencyGraphTransformer {
    private final BdioPropertyHelper bdioPropertyHelper;
    private final BdioNodeFactory bdioNodeFactory;

    public DependencyGraphTransformer(BdioPropertyHelper bdioPropertyHelper, BdioNodeFactory bdioNodeFactory) {
        this.bdioPropertyHelper = bdioPropertyHelper;
        this.bdioNodeFactory = bdioNodeFactory;
    }

    public List<BdioComponent> transformDependencyGraph(
        DependencyGraph graph,
        BdioNode currentNode,
        Set<Dependency> dependencies,
        Map<ExternalId, BdioNode> existingComponents
    ) {
        List<BdioComponent> addedComponents = new ArrayList<>();
        for (Dependency dependency : dependencies) {
            if (!existingComponents.containsKey(dependency.getExternalId())) {
                BdioComponent addedNode = componentFromDependency(dependency);
                addedComponents.add(addedNode);
                existingComponents.put(dependency.getExternalId(), addedNode);
                List<BdioComponent> addedChildren = transformDependencyGraph(graph, addedNode, graph.getChildrenForParent(dependency), existingComponents);
                addedComponents.addAll(addedChildren);
            }
            bdioPropertyHelper.addRelationship(currentNode, existingComponents.get(dependency.getExternalId()));
        }

        return addedComponents;
    }

    private BdioComponent componentFromDependency(Dependency dependency) {
        String componentName = dependency.getName();
        String componentVersion = dependency.getVersion();
        BdioId componentId = dependency.getExternalId().createBdioId();
        BdioExternalIdentifier componentExternalIdentifier = bdioPropertyHelper.createExternalIdentifier(dependency.getExternalId());

        BdioComponent component = bdioNodeFactory.createComponent(componentName, componentVersion, componentId, componentExternalIdentifier);
        return component;
    }

}
