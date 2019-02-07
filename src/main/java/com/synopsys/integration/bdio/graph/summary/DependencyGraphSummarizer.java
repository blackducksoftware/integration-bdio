/**
 * integration-bdio
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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
package com.synopsys.integration.bdio.graph.summary;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.google.gson.Gson;
import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.model.BdioId;
import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.util.NameVersion;

public class DependencyGraphSummarizer {
    private final Gson gson;

    public DependencyGraphSummarizer(Gson gson) {
        this.gson = gson;
    }

    public GraphSummary fromJson(String data) {
        return gson.fromJson(data, GraphSummary.class);
    }

    public String toJson(GraphSummary data) {
        return gson.toJson(data);
    }

    public String toJson(DependencyGraph graph) {
        return toJson(fromGraph(graph));
    }

    public GraphSummary fromGraph(DependencyGraph graph) {
        Queue<Dependency> unprocessed = new LinkedList<>(graph.getRootDependencies());
        Set<Dependency> processed = new HashSet<>();

        GraphSummary graphSummary = new GraphSummary();

        while (unprocessed.size() > 0) {
            Dependency nextDependency = unprocessed.remove();
            processed.add(nextDependency);

            BdioId nextId = nextDependency.externalId.createBdioId();
            if (!graphSummary.dependencySummaries.containsKey(nextId)) {
                NameVersion nameVersion = new NameVersion();
                nameVersion.setName(nextDependency.name);
                nameVersion.setVersion(nextDependency.version);
                graphSummary.dependencySummaries.put(nextId, nameVersion);
            }

            for (Dependency dep : graph.getChildrenForParent(nextDependency)) {
                if (!graphSummary.externalDataIdRelationships.containsKey(nextId)) {
                    graphSummary.externalDataIdRelationships.put(nextId, new HashSet<>());
                }
                graphSummary.externalDataIdRelationships.get(nextId).add(dep.externalId.createBdioId());
                if (!processed.contains(dep)) {
                    unprocessed.add(dep);
                }
            }
        }

        for (ExternalId externalId : graph.getRootDependencyExternalIds()) {
            graphSummary.rootExternalDataIds.add(externalId.createBdioId());
        }

        return graphSummary;
    }

}
