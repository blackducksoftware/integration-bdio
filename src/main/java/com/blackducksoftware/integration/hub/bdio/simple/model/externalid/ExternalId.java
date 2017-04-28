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
package com.blackducksoftware.integration.hub.bdio.simple.model.externalid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.blackducksoftware.integration.hub.bdio.simple.model.Forge;

public abstract class ExternalId {
    public static final String DATA_ID_SEPARATOR = "/";

    public final Forge forge;

    public ExternalId(final Forge forge) {
        this.forge = forge;
    }

    public abstract String[] getExternalIdPieces();

    public String createDataId() {
        final List<String> dataIdPieces = new ArrayList<>();
        dataIdPieces.add(forge.toString());
        dataIdPieces.addAll(escapePiecesForUri(Arrays.asList(getExternalIdPieces())));

        return "data:" + StringUtils.join(dataIdPieces, DATA_ID_SEPARATOR);
    }

    public String createExternalId() {
        return StringUtils.join(getExternalIdPieces(), forge.separator);
    }

    /**
     * Do a poor man's URI escaping. We aren't terribly interested in precision here, or in introducing a library that
     * would do it better.
     */
    private List<String> escapePiecesForUri(final List<String> pieces) {
        final List<String> escapedPieces = new ArrayList<>(pieces.size());
        for (final String piece : pieces) {
            final String escaped = piece.replaceAll("[^A-Za-z0-9]", "_");
            escapedPieces.add(escaped);
        }

        return escapedPieces;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, RecursiveToStringStyle.JSON_STYLE);
    }

}