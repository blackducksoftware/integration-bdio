/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model.dependency;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public int hashCode() {
        return Objects.hash(name, version, externalId, scope);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dependency)) {
            return false;
        }
        Dependency that = (Dependency) obj;
        return Objects.equals(name, that.name) &&
               Objects.equals(version, that.version) &&
               Objects.equals(externalId, that.externalId) &&
               Objects.equals(scope, that.scope);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("name", name)
            .append("version", version)
            .append("externalId", externalId)
            .append("scope", scope)
            .build();
    }
}
