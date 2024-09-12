/*
 * integration-bdio
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.bdio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BdioCreationInfo {
    @SerializedName("spdx:creator")
    private final List<String> creator = new ArrayList<>();

    @SerializedName("spdx:created")
    public String created;

    // ekerwin 2018-06-11
    // Black Duck only supports a single creator and if there are multiple creators, Black Duck will use only the first one.
    public void setPrimarySpdxCreator(final SpdxCreator spdxCreator) {
        creator.add(0, spdxCreator.getData());
    }

    public void addSpdxCreator(final SpdxCreator spdxCreator) {
        creator.add(spdxCreator.getData());
    }

    public List<String> getCreator() {
        return Collections.unmodifiableList(creator);
    }

}
