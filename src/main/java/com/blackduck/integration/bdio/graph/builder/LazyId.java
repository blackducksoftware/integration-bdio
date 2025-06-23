/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.graph.builder;

import com.blackduck.integration.util.NameVersion;
import com.blackduck.integration.util.Stringable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.blackduck.integration.bdio.graph.builder.LazyIdSource.*;

/**
 * An id for a Dependency that has fewer requirements than an ExternalId. While
 * building a graph you may have a name and a version, or maybe just a name, or
 * perhaps just a plain string to identify the Dependency.
 */
public class LazyId extends Stringable {
    public static LazyId fromName(String name) {
        return new LazyId(NAME, Arrays.asList(name));
    }

    public static LazyId fromNameAndVersion(String name, String version) {
        return new LazyId(NAME_VERSION, Arrays.asList(name, version));
    }

    public static LazyId fromNameVersion(NameVersion nameVersion) {
        return new LazyId(NAME_VERSION, Arrays.asList(nameVersion.getName(), nameVersion.getVersion()));
    }

    public static LazyId fromString(String s) {
        return new LazyId(STRING, Arrays.asList(s));
    }

    private final List<String> pieces = new LinkedList<>();

    public LazyId(LazyIdSource source, List<String> pieces) {
        this.pieces.add(source.name());
        this.pieces.addAll(pieces);
    }

    @Override
    public int hashCode() {
        return pieces.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LazyId))
            return false;

        final LazyId other = (LazyId) obj;
        return pieces.equals(other.pieces);
    }

    /**
     * Since the only instance variable never gets modified, we store toString() results for efficiency.
     */
    private String toStringResult;

    @Override
    public String toString() {
        if (toStringResult == null) {
            toStringResult = new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("pieces", pieces)
                .build();
        }
        return toStringResult;
    }
}
