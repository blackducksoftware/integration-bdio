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

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.junit.Assert;
import org.junit.Test;

import com.blackducksoftware.integration.hub.bdio.simple.model.DependencyNode;
import com.blackducksoftware.integration.hub.bdio.simple.model.externalid.ExternalId;
import com.blackducksoftware.integration.hub.bdio.simple.model.externalid.ExternalIdFactory;

public class DependencyNodeBuilderTest {
    private final ExternalIdFactory externalIdFactory = new ExternalIdFactory();

    DependencyNode root = new DependencyNode("root", "1.0", externalIdFactory.createMavenExternalId("testRoot", "root", "1.0"));
    DependencyNode firstChild = new DependencyNode("first", "1.0", externalIdFactory.createMavenExternalId("children", "first", "1.0"));
    DependencyNode secondChild = new DependencyNode("second", "2.0", externalIdFactory.createMavenExternalId("children", "second", "2.0"));
    DependencyNode thirdChild = new DependencyNode("third", "3.0", externalIdFactory.createMavenExternalId("children", "third", "3.0"));
    DependencyNode fourthChild = new DependencyNode("fourth", "4.0", externalIdFactory.createMavenExternalId("children", "fourth", "4.0"));
    DependencyNode subFirstChild = new DependencyNode("first", "1.0", externalIdFactory.createMavenExternalId("subChild", "first", "1.0"));
    DependencyNode subSecondChild = new DependencyNode("second", "2.0", externalIdFactory.createMavenExternalId("subChild", "second", "2.0"));
    DependencyNode subThirdChild = new DependencyNode("third", "3.0", externalIdFactory.createMavenExternalId("subChild", "third", "3.0"));

    private DependencyNode getRootNodeToCompareWith() {
        // Constructing the root node in a specific structure
        final LinkedHashSet<DependencyNode> children = new LinkedHashSet<>();
        firstChild.children.addAll(Arrays.asList(subFirstChild, subSecondChild));
        secondChild.children.add(subThirdChild);
        fourthChild.children.addAll(Arrays.asList(subFirstChild, subSecondChild, subThirdChild));

        children.add(firstChild);
        children.add(secondChild);
        children.add(thirdChild);
        children.add(fourthChild);

        root.children = children;

        return root;
    }

    @Test
    public void testDependencyNodeBuilder() {
        final DependencyNode rootToCompareTo = getRootNodeToCompareWith();

        // Adding the relationships randomly
        final DependencyNodeBuilder builder = new DependencyNodeBuilder(root);
        builder.addParentNodeWithChildren(secondChild, Arrays.asList(subThirdChild));
        builder.addChildNodeWithParents(subSecondChild, Arrays.asList(fourthChild, firstChild));
        builder.addChildNodeWithParents(subFirstChild, Arrays.asList(fourthChild, firstChild));
        builder.addParentNodeWithChildren(root, Arrays.asList(firstChild, secondChild, thirdChild, fourthChild));
        builder.addChildNodeWithParents(subThirdChild, Arrays.asList(fourthChild));

        compareNode(rootToCompareTo, root);
    }

    private void compareNode(final DependencyNode expected, final DependencyNode actual) {
        Assert.assertEquals(expected.name, actual.name);
        Assert.assertEquals(expected.version, actual.version);
        Assert.assertEquals(expected.externalId.createBdioId(), actual.externalId.createBdioId());
        Assert.assertEquals(expected.children.size(), actual.children.size());

        for (final DependencyNode expectedChild : expected.children) {
            final String expectedBdioId = expectedChild.externalId.createBdioId();
            boolean foundMatch = false;
            for (final DependencyNode actualChild : actual.children) {
                if (expectedBdioId.equals(actualChild.externalId.createBdioId())) {
                    foundMatch = true;
                    compareNode(expectedChild, actualChild);
                }
            }
            Assert.assertTrue(foundMatch);
        }
    }

    @Test
    public void testDependencyNodeBuilderCorrectsOnRebuild() {
        // in the case of a tree where the left and right side have different nodes with different children
        // rebuild should reconcile that and both nodes should get both children.
        // note this doesn't 'fix' both nodes - it fixes the tree
        // so in the case below sharedright will be removed from the tree
        // and sharedleft will occur twice with 2 kids
        // this is because shared left is encountered first so it becomes the accumulator.
        final DependencyNode root = makeNode("root", "root", "root");
        final DependencyNode left = makeNode("left", "left", "left");
        final DependencyNode right = makeNode("right", "right", "right");
        final DependencyNode sharedright = makeNode("shared", "shared", "shared");
        final DependencyNode sharedleft = makeNode("shared", "shared", "shared");
        final DependencyNode sharedleftkid = makeNode("kidleft", "kidleft", "kidleft");
        final DependencyNode sharedrightkid = makeNode("kidright", "kidright", "kidright");

        left.children.add(sharedleft);
        sharedleft.children.add(sharedleftkid);

        right.children.add(sharedright);
        sharedright.children.add(sharedrightkid);

        root.children.add(left);
        root.children.add(right);

        final DependencyNodeBuilder builder = new DependencyNodeBuilder(root);

        builder.rebuild();

        Assert.assertEquals(2, sharedleft.children.size());
    }

    DependencyNode makeNode(final String org, final String mod, final String rev) {
        final ExternalId id = externalIdFactory.createMavenExternalId(org, mod, rev);
        final DependencyNode node = new DependencyNode(mod, rev, id);
        return node;
    }
}