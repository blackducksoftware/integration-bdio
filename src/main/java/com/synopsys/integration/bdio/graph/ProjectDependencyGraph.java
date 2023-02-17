/*
 * integration-bdio
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.bdio.graph;

import java.util.Set;

import org.apache.commons.collections4.SetUtils;

import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.dependency.ProjectDependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;

public class ProjectDependencyGraph extends DependencyGraph {
    private final ProjectDependency projectDependency;

    public ProjectDependencyGraph(Dependency projectDependency) {
        this.projectDependency = new ProjectDependency(projectDependency);
    }

    public ProjectDependencyGraph(ProjectDependency projectDependency) {
        this.projectDependency = projectDependency;
    }

    public ProjectDependencyGraph(Forge forge, String relativePath) {
        this(new ProjectDependency(ExternalId.FACTORY.createPathExternalId(forge, relativePath)));
    }

    public ProjectDependency getProjectDependency() {
        return projectDependency;
    }

    @Override
    public void addDirectDependency(Dependency child) {
        addParentWithChild(projectDependency, child);
    }

    @Override
    public Set<Dependency> getDirectDependencies() {
        return getChildrenForParent(projectDependency);
    }

    @Override
    public Set<Dependency> getRootDependencies() {
        return SetUtils.hashSet(projectDependency);
    }
}
