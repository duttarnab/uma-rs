package org.xdi.oxd.rs.protect;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Yuriy Zabrovarnyy
 */

public class ResourceValidatorTest {

    @Test
    public void httpMethodMustBeUniqueWithinPath() throws IOException {
        assertTrue(ResourceValidator.isHttpMethodUniqueInPath(RsProtector.read(fileInputStream("valid.json")).getResources()));
        assertFalse(ResourceValidator.isHttpMethodUniqueInPath(RsProtector.read(fileInputStream("duplicated-http-method.json")).getResources()));
    }

    private InputStream fileInputStream(String fileName) {
        return ResourceValidatorTest.class.getResourceAsStream(fileName);
    }
}
