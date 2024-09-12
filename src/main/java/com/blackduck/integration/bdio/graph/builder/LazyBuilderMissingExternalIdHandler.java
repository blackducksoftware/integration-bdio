/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.graph.builder;

import com.blackduck.integration.bdio.model.externalid.ExternalId;

@FunctionalInterface
public interface LazyBuilderMissingExternalIdHandler {
    ExternalId handleMissingExternalId(final LazyId lazyId, final LazyExternalIdDependencyGraphBuilder.LazyDependencyInfo lazyDependencyInfo) throws MissingExternalIdException;

}
