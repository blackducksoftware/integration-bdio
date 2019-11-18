package com.synopsys.integration.bdio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.bdio1.model.BdioCreationInfo;
import com.synopsys.integration.bdio.bdio1.model.SpdxCreator;

public class BdioCreationInfoTest {
    @Test
    public void testAddingMultipleCreators() {
        final BdioCreationInfo bdioCreationInfo = new BdioCreationInfo();
        bdioCreationInfo.addSpdxCreator(SpdxCreator.createToolSpdxCreator("a simple tool", "2.0"));
        bdioCreationInfo.addSpdxCreator(SpdxCreator.createToolSpdxCreator("another simple tool", "1.0"));

        assertEquals(2, bdioCreationInfo.getCreator().size());
        assertEquals("Tool: a simple tool-2.0", bdioCreationInfo.getCreator().get(0));
    }

    @Test
    public void testSettingPrimaryCreator() {
        final BdioCreationInfo bdioCreationInfo = new BdioCreationInfo();
        bdioCreationInfo.addSpdxCreator(SpdxCreator.createToolSpdxCreator("a simple tool", "2.0"));
        bdioCreationInfo.setPrimarySpdxCreator(SpdxCreator.createToolSpdxCreator("another simple tool", "1.0"));

        assertEquals(2, bdioCreationInfo.getCreator().size());
        assertEquals("Tool: another simple tool-1.0", bdioCreationInfo.getCreator().get(0));
    }

}
