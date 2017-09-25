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
package com.blackducksoftware.integration.hub.bdio.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blackducksoftware.integration.hub.bdio.model.dependency.Dependency;
import com.blackducksoftware.integration.hub.bdio.model.externalid.ExternalId;

public class DependencyGraphBuilder {
    final Set<ExternalId> rootDependencies = new HashSet<>();
    final Map<ExternalId, Dependency> dependencies = new HashMap<>();
    final Map<ExternalId, Set<ExternalId>> relationships = new HashMap<>();

    public DependencyGraph build() {
        return new MapDependencyGraph(rootDependencies, dependencies, relationships);
    }

    private void ensureDependencyExists(final Dependency dependency) {
        if (!dependencies.containsKey(dependency.externalId)) {
            dependencies.put(dependency.externalId, dependency);
        }
    }

    private void ensureDependencyAndRelationshipExists(final Dependency dependency) {
        ensureDependencyExists(dependency);
        if (!relationships.containsKey(dependency.externalId)) {
            relationships.put(dependency.externalId, new HashSet<ExternalId>());
        }
    }

    private void addRelationship(final Dependency parent, final Dependency child) {
        relationships.get(parent.externalId).add(child.externalId);
    }

    private Set<Dependency> dependenciesFromExternalIds(final Set<ExternalId> ids) {
        final Set<Dependency> foundDependencies = new HashSet<>();
        for (final ExternalId id : ids) {
            if (dependencies.containsKey(id)) {
                foundDependencies.add(dependencies.get(id));
            }
        }
        return foundDependencies;
    }

    public boolean hasDependency(final ExternalId dependency) {
        return dependencies.containsKey(dependency);
    }

    public boolean hasDependency(final Dependency dependency) {
        return dependencies.containsKey(dependency.externalId);
    }

    public Dependency getDependency(final ExternalId dependency) {
        if (dependencies.containsKey(dependency)) {
            return dependencies.get(dependency);
        }
        return null;
    }

    public Set<Dependency> getChildrenForParent(final ExternalId parent) {
        final Set<ExternalId> childIds = getChildrenExternalIdsForParent(parent);
        return dependenciesFromExternalIds(childIds);
    }

    public Set<Dependency> getParentsForChild(final ExternalId child) {
        final Set<ExternalId> parentIds = getParentExternalIdsForChild(child);
        return dependenciesFromExternalIds(parentIds);
    }

    public Set<ExternalId> getChildrenExternalIdsForParent(final ExternalId parent) {
        final Set<ExternalId> children = new HashSet<>();
        if (relationships.containsKey(parent)) {
            for (final ExternalId id : relationships.get(parent)) {
                children.add(id);
            }
        }
        return children;
    }

    public Set<ExternalId> getParentExternalIdsForChild(final ExternalId child) {
        final Set<ExternalId> parents = new HashSet<>();
        for (final ExternalId parentId : relationships.keySet()) {
            for (final ExternalId childId : relationships.get(parentId)) {
                if (childId.equals(child)) {
                    parents.add(parentId);
                }
            }
        }
        return parents;
    }

    public Set<Dependency> getChildrenForParent(final Dependency parent) {
        return getChildrenForParent(parent.externalId);
    }

    public Set<Dependency> getParentsForChild(final Dependency child) {
        return getParentsForChild(child.externalId);
    }

    public Set<ExternalId> getChildrenExternalIdsForParent(final Dependency parent) {
        return getChildrenExternalIdsForParent(parent.externalId);
    }

    public Set<ExternalId> getParentExternalIdsForChild(final Dependency child) {
        return getParentExternalIdsForChild(child.externalId);
    }

    public void addParentWithChild(final Dependency parent, final Dependency child) {
        ensureDependencyAndRelationshipExists(parent);
        ensureDependencyExists(child);
        addRelationship(parent, child);
    }

    public void addChildWithParent(final Dependency child, final Dependency parent) {
        addParentWithChild(parent, child);
    }

    public void addParentWithChildren(final Dependency parent, final List<Dependency> children) {
        ensureDependencyAndRelationshipExists(parent);
        for (final Dependency child : children) {
            ensureDependencyExists(child);
            addRelationship(parent, child);
        }
    }

    public void addChildWithParents(final Dependency child, final List<Dependency> parents) {
        ensureDependencyExists(child);
        for (final Dependency parent : parents) {
            ensureDependencyAndRelationshipExists(parent);
            addRelationship(parent, child);
        }

    }

    public void addParentWithChildren(final Dependency parent, final Set<Dependency> children) {
        ensureDependencyAndRelationshipExists(parent);
        for (final Dependency child : children) {
            ensureDependencyExists(child);
            addRelationship(parent, child);
        }
    }

    public void addChildWithParents(final Dependency child, final Set<Dependency> parents) {
        ensureDependencyExists(child);
        for (final Dependency parent : parents) {
            ensureDependencyAndRelationshipExists(parent);
            addRelationship(parent, child);
        }
    }

    public void addParentWithChildren(final Dependency parent, final Dependency... children) {
        addParentWithChildren(parent, Arrays.asList(children));
    }

    public void addChildWithParents(final Dependency child, final Dependency... parents) {
        addChildWithParents(child, Arrays.asList(parents));
    }

    public Set<ExternalId> getRootDependencyExternalIds() {
        final HashSet<ExternalId> copy = new HashSet<>();
        copy.addAll(rootDependencies);
        return copy;
    }

    public Set<Dependency> getRootDependencies() {
        return dependenciesFromExternalIds(getRootDependencyExternalIds());
    }

    public void addChildToRoot(final Dependency child) {
        ensureDependencyExists(child);
        rootDependencies.add(child.externalId);
    }

    public void addChildrenToRoot(final List<Dependency> children) {
        for (final Dependency child : children) {
            addChildToRoot(child);
        }
    }

    public void addChildrenToRoot(final Set<Dependency> children) {
        for (final Dependency child : children) {
            addChildToRoot(child);
        }
    }

    public void addChildrenToRoot(final Dependency... children) {
        for (final Dependency child : children) {
            addChildToRoot(child);
        }
    }

}