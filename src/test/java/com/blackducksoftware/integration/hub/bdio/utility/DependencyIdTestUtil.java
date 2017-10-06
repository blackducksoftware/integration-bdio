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
package com.blackducksoftware.integration.hub.bdio.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.blackducksoftware.integration.hub.bdio.model.dependencyid.DependencyId;

public class DependencyIdTestUtil {
    public static Set<DependencyId> asSet(final DependencyId... dependencies) {
        final Set<DependencyId> set = new HashSet<>();
        for (final DependencyId dependency : dependencies) {
            set.add(dependency);
        }
        return set;
    }

    public static List<DependencyId> asList(final DependencyId... dependencies) {
        final List<DependencyId> list = new ArrayList<>();
        for (final DependencyId dependency : dependencies) {
            list.add(dependency);
        }
        return list;
    }

}
