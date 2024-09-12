/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BdioNode {
    @SerializedName("@id")
    public BdioId id;

    @SerializedName("@type")
    public String type;

    @SerializedName("externalIdentifier")
    public BdioExternalIdentifier bdioExternalIdentifier;

    @SerializedName("relationship")
    public List<BdioRelationship> relationships = new ArrayList<>();

}
