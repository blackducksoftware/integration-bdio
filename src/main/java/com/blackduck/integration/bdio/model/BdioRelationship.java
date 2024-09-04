/*
 * integration-bdio
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import com.google.gson.annotations.SerializedName;

public class BdioRelationship {
    @SerializedName("related")
    public BdioId related;

    @SerializedName("relationshipType")
    public String relationshipType;

}
