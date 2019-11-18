package com.synopsys.integration.bdio.model.externalid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.bdio1.SimpleBdioFactory;
import com.synopsys.integration.bdio.model.BdioId;
import com.synopsys.integration.bdio.model.Forge;

public class ExternalIdTest {
    private final SimpleBdioFactory simpleBdioFactory = new SimpleBdioFactory();

    @Test
    public void testForgeEquality() {
        assertEquals(Forge.ANACONDA, new Forge("/", "anaconda"));
    }

    @Test
    public void testCreatingExternalIds() {
        ExternalId architectureExternalId = simpleBdioFactory.createArchitectureExternalId(Forge.CENTOS, "name", "version", "architecture");
        assertEquals(new BdioId("http:centos/name/version/architecture"), architectureExternalId.createBdioId());
        assertEquals("name/version/architecture", architectureExternalId.createExternalId());

        ExternalId mavenExternalId = simpleBdioFactory.createMavenExternalId("group", "artifact", "version");
        assertEquals(new BdioId("http:maven/group/artifact/version"), mavenExternalId.createBdioId());
        assertEquals("group:artifact:version", mavenExternalId.createExternalId());

        ExternalId moduleNamesExternalId = simpleBdioFactory.createModuleNamesExternalId(Forge.CPAN, "name", "version", "something", "else");
        assertEquals(new BdioId("http:cpan/name/version/something/else"), moduleNamesExternalId.createBdioId());
        assertEquals("name/version/something/else", moduleNamesExternalId.createExternalId());

        ExternalId nameVersionExternalId = simpleBdioFactory.createNameVersionExternalId(Forge.PYPI, "name", "version");
        assertEquals(new BdioId("http:pypi/name/version"), nameVersionExternalId.createBdioId());
        assertEquals("name/version", nameVersionExternalId.createExternalId());

        ExternalId pathExternalId = simpleBdioFactory.createPathExternalId(Forge.GOGET, "name");
        assertEquals(new BdioId("http:goget/name"), pathExternalId.createBdioId());
        assertEquals("name", pathExternalId.createExternalId());

        ExternalId yoctoExternalId = simpleBdioFactory.createYoctoExternalId("layer", "name", "version");
        assertEquals(new BdioId("http:yocto/layer/name/version"), yoctoExternalId.createBdioId());
        assertEquals("layer/name/version", yoctoExternalId.createExternalId());
    }

    @Test
    public void testEscapingBadUriCharacters() {
        ExternalId nameVersionExternalId = simpleBdioFactory.createNameVersionExternalId(Forge.NPMJS, "name with spaces", "version with a - and a # and spaces");
        assertEquals(new BdioId("http:npmjs/name_with_spaces/version_with_a___and_a___and_spaces"), nameVersionExternalId.createBdioId());
        assertEquals("name with spaces/version with a - and a # and spaces", nameVersionExternalId.createExternalId());
    }

    @Test
    public void testWithoutEnoughState() {
        ExternalId externalId = new ExternalId(Forge.MAVEN);
        externalId.setName("bundler");
        String[] pieces = externalId.getExternalIdPieces();
        assertEquals(1, pieces.length);
        assertEquals("bundler", pieces[0]);
    }

    @Test
    public void testCreateBdioId() {
        ExternalId externalId = new ExternalId(Forge.MAVEN);
        externalId.setName("testName");
        externalId.setVersion("testVersion");
        assertEquals(new BdioId("http:maven/testName/testVersion"), externalId.createBdioId());
    }

    @Test
    public void testBoilerplateCode() {
        ExternalId externalIdA = simpleBdioFactory.createMavenExternalId("group", "artifact", "version");
        ExternalId externalIdB = simpleBdioFactory.createMavenExternalId("group", "artifact", "version");
        assertEquals(externalIdA, externalIdB);
        assertEquals(externalIdA.hashCode(), externalIdB.hashCode());
        assertEquals(externalIdA.toString(), externalIdB.toString());
    }

    @Test
    public void testMavenWithoutVersion() {
        ExternalId externalId = simpleBdioFactory.createMavenExternalId("thegroup", "thename", null);
        assertEquals("thegroup:thename", externalId.createExternalId());
        assertEquals(new BdioId("http:maven/thegroup/thename"), externalId.createBdioId());
    }

    @Test
    public void testNameWithoutVersion() {
        ExternalId externalId = simpleBdioFactory.createNameVersionExternalId(Forge.RUBYGEMS, "thename", null);
        assertEquals("thename", externalId.createExternalId());
        assertEquals(new BdioId("http:rubygems/thename"), externalId.createBdioId());
    }

    @Test
    public void testYoctoWithoutVersion() {
        ExternalId externalId = simpleBdioFactory.createYoctoExternalId("thelayer", "thename", null);
        assertEquals("thelayer/thename", externalId.createExternalId());
        assertEquals(new BdioId("http:yocto/thelayer/thename"), externalId.createBdioId());
    }

    @Test
    public void testUnexpectedState() {
        ExternalId externalId = new ExternalId(Forge.PYPI);
        externalId.setVersion("1.0.0");
        externalId.setArchitecture("i586");
        assertEquals("1.0.0/i586", externalId.createExternalId());
        assertEquals(new BdioId("http:pypi/1_0_0/i586"), externalId.createBdioId());
    }

}
