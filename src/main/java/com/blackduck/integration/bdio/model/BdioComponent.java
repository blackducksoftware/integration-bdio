/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import com.google.gson.annotations.SerializedName;

public class BdioComponent extends BdioNode {
    @SerializedName("name")
    public String name;

    @SerializedName("revision")
    public String version;

    public BdioComponent() {
        type = "Component";
    }

}
