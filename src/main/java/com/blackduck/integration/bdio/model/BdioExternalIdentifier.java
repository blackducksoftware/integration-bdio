/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import com.google.gson.annotations.SerializedName;
import com.blackduck.integration.bdio.model.externalid.ExternalId;

public class BdioExternalIdentifier {
    @SerializedName("externalSystemTypeId")
    public String forge;

    @SerializedName("externalId")
    public String externalId;

    @SerializedName("externalIdMetaData")
    // this horrible name exists because 'externalId' is reserved by the bdio specification
    public ExternalId externalIdMetaData;
}
