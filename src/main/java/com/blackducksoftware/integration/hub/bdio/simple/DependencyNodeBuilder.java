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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blackducksoftware.integration.hub.bdio.simple.model.DependencyNode;

public class DependencyNodeBuilder {
    final Map<String, DependencyNode> nodes = new HashMap<>();

    final DependencyNode root;

    public DependencyNodeBuilder(final DependencyNode root) {
        this.root = root;
        nodes.put(root.externalId.createBdioId(), root);
    }

    public void addParentNodeWithChildren(final DependencyNode parent, final List<DependencyNode> children) {
        addParentNodeWithChildren(parent, new LinkedHashSet<>(children));
    }

    public void addChildNodeWithParents(final DependencyNode child, final List<DependencyNode> parents) {
        addChildNodeWithParents(child, new LinkedHashSet<>(parents));
    }

    public void addParentNodeWithChildren(final DependencyNode parent, final Set<DependencyNode> children) {
        if (!nodes.containsKey(parent.externalId.createBdioId())) {
            nodes.put(parent.externalId.createBdioId(), parent);
        }

        for (final DependencyNode child : children) {
            if (!nodes.containsKey(child.externalId.createBdioId())) {
                nodes.put(child.externalId.createBdioId(), child);
            }

            // here, we are looping over the children, so 'child' will be different every time, but parent will always
            // be the same
            nodes.get(parent.externalId.createBdioId()).children.add(nodes.get(child.externalId.createBdioId()));
        }
    }

    public void addChildNodeWithParents(final DependencyNode child, final Set<DependencyNode> parents) {
        if (!nodes.containsKey(child.externalId.createBdioId())) {
            nodes.put(child.externalId.createBdioId(), child);
        }

        for (final DependencyNode parent : parents) {
            if (!nodes.containsKey(parent.externalId.createBdioId())) {
                nodes.put(parent.externalId.createBdioId(), parent);
            }

            // here, we are looping over the parents, so 'parent' will be different every time, but child will always be
            // the same
            nodes.get(parent.externalId.createBdioId()).children.add(nodes.get(child.externalId.createBdioId()));
        }
    }

    public void rebuild() {
        rebuild(root);
    }

    public void rebuild(final DependencyNode node) {
        addParentNodeWithChildren(node, node.children);
        for (final DependencyNode child : node.children) {
            rebuild(child);
        }
    }

}