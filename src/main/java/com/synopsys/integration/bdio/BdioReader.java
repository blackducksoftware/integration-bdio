/*
 * integration-bdio
 *
 * Copyright (c) 2021 Synopsys, Inc.
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
package com.synopsys.integration.bdio;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.synopsys.integration.bdio.model.BdioBillOfMaterials;
import com.synopsys.integration.bdio.model.BdioComponent;
import com.synopsys.integration.bdio.model.BdioProject;
import com.synopsys.integration.bdio.model.SimpleBdioDocument;

public class BdioReader implements Closeable {
    private final Gson gson;
    private final JsonReader jsonReader;

    public BdioReader(final Gson gson, final Reader reader) throws IOException {
        this.gson = gson;
        jsonReader = new JsonReader(reader);
        jsonReader.beginArray();
    }

    public BdioReader(final Gson gson, final InputStream inputStream) throws IOException {
        this.gson = gson;
        jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        jsonReader.beginArray();
    }

    public SimpleBdioDocument readSimpleBdioDocument() throws IOException {
        final SimpleBdioDocument document = new SimpleBdioDocument();
        document.setBillOfMaterials(readBillOfMaterials());
        document.setProject(readProject());
        while (jsonReader.hasNext()) {
            document.getComponents().add(readComponent());
        }
        return document;
    }

    public BdioBillOfMaterials readBillOfMaterials() {
        return gson.fromJson(jsonReader, BdioBillOfMaterials.class);
    }

    public BdioProject readProject() {
        return gson.fromJson(jsonReader, BdioProject.class);
    }

    public BdioComponent readComponent() {
        return gson.fromJson(jsonReader, BdioComponent.class);
    }

    @Override
    public void close() throws IOException {
        jsonReader.endArray();
        jsonReader.close();
    }

}
