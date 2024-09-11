/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.blackduck.integration.bdio.model.dependency.Dependency;
import com.blackduck.integration.bdio.model.externalid.ExternalId;

public class BasicDependencyGraph extends DependencyGraph {
    private final Set<ExternalId> directDependencies = new HashSet<>();

    @Override
    public Set<Dependency> getDirectDependencies() {
        return directDependencies.stream()
            .map(dependencies::get)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Dependency> getRootDependencies() {
        return getDirectDependencies();
    }

    @Override
    public void addDirectDependency(Dependency child) {
        ensureDependencyExists(child);
        directDependencies.add(child.getExternalId());
    }
}
