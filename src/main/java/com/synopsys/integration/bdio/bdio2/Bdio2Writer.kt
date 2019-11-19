/**
 * integration-bdio
 *
 * Copyright (c) 2019 Synopsys, Inc.
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
package com.synopsys.integration.bdio.bdio2

import com.blackducksoftware.bdio2.BdioMetadata
import com.blackducksoftware.bdio2.BdioWriter
import com.blackducksoftware.bdio2.model.Component
import com.blackducksoftware.bdio2.model.Project
import java.io.IOException
import java.io.OutputStream

class Bdio2Writer {

    @Throws(IOException::class)
    fun writeBdioDocument(outputStream: OutputStream, bdio2Document: Bdio2Document) {
        val bdioWriter: BdioWriter = createBdioWriter(outputStream, bdio2Document.bdioMetadata)
        writeBdioDocument(bdioWriter, bdio2Document.bdioProject, bdio2Document.components)
    }

    private fun createBdioWriter(outputStream: OutputStream, bdioMetadata: BdioMetadata): BdioWriter {
        val streamSupplier = BdioWriter.BdioFile(outputStream)
        return BdioWriter(bdioMetadata, streamSupplier)
    }

    @Throws(IOException::class)
    fun writeBdioDocument(bdioWriter: BdioWriter, project: Project, components: List<Component>) {
        bdioWriter.start()

        components.forEach { bdioWriter.next(it) }

        // We put the project node at the end of the document to be more inline with the way Black Duck produces BDIO 2.
        bdioWriter.next(project)

        bdioWriter.close()
    }
}