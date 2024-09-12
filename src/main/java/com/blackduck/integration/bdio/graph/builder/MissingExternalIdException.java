/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.graph.builder;

import com.blackduck.integration.exception.IntegrationException;

public class MissingExternalIdException extends IntegrationException {
    public MissingExternalIdException(LazyId lazyId) {
        super(String.format("A dependency (%s) in a relationship in the graph never had it's external id set.", lazyId));
    }

}
