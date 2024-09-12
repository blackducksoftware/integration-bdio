/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model.dependency;

import com.blackduck.integration.bdio.model.externalid.ExternalId;
import com.blackduck.integration.util.Stringable;

public class Dependency extends Stringable {
    public static final DependencyFactory FACTORY = new DependencyFactory();

    private String name;

    private String version;

    private ExternalId externalId;

    private String scope;

    public Dependency(String name, String version, ExternalId externalId, String scope) {
        this.name = name;
        this.version = version;
        this.externalId = externalId;
        this.scope = scope;
    }

    public Dependency(String name, ExternalId externalId, String scope) {
        this(name, externalId.getVersion(), externalId, scope);
    }

    public Dependency(ExternalId externalId, String scope) {
        this(externalId.getName(), externalId.getVersion(), externalId, scope);
    }

    /**
     * @deprecated Use {@link #Dependency(String, String, ExternalId, String)} instead
     */
    @Deprecated
    public Dependency(String name, String version, ExternalId externalId) {
        this(name, version, externalId, null);
    }

    /**
     * @deprecated Use {@link #Dependency(String, ExternalId, String)} instead
     */
    @Deprecated
    public Dependency(String name, ExternalId externalId) {
        this(name, externalId.getVersion(), externalId, null);
    }

    /**
     * @deprecated Use {@link #Dependency(ExternalId, String)} instead
     */
    @Deprecated
    public Dependency(ExternalId externalId) {
        this(externalId.getName(), externalId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ExternalId getExternalId() {
        return externalId;
    }

    public void setExternalId(ExternalId externalId) {
        this.externalId = externalId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
