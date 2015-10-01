package com.qmetric.pdf.manipulation;

import com.itextpdf.text.pdf.PdfReader;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ITextPasswordProtectionTest {

    @Test
    public void shouldAddPasswordProtection() throws Exception {
        final byte[] unprotected = TestResources.get(TestResources.get("/unlocked.pdf"));

        final byte[] bytes = new ITextPasswordProtection().addProtection(unprotected, "test".getBytes());

        assertTrue(new PdfReader(bytes, "test".getBytes()).isEncrypted());
    }

    @Test
    public void shouldRemovePasswordProtection() throws Exception {
        final byte[] protectedPdf = TestResources.get(TestResources.get("/locked.pdf"));

        final byte[] bytes = new ITextPasswordProtection().removeProtection(protectedPdf, "test".getBytes());

        assertFalse(new PdfReader(bytes, "".getBytes()).isEncrypted());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowErrorWhenPasswordIncorrect() throws Exception {
        final byte[] protectedPdf = TestResources.get(TestResources.get("/locked.pdf"));

        new ITextPasswordProtection().removeProtection(protectedPdf, "incorrect".getBytes());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowErrorWhenAddingPasswordToPDFAlreadyProtected() throws Exception {
        final byte[] protectedPdf = TestResources.get(TestResources.get("/locked.pdf"));

        new ITextPasswordProtection().addProtection(protectedPdf, "new password".getBytes());
    }
}