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