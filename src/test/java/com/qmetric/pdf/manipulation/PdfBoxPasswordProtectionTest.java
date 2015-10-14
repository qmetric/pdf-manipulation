package com.qmetric.pdf.manipulation;

import com.qmetric.pdf.manipulation.pdfbox.PdfBoxPasswordProtection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PdfBoxPasswordProtectionTest {

    @Test
    public void addProtection() throws Exception {
        final byte[] original = TestResources.get(TestResources.get("/unlocked.pdf"));

        try (ByteArrayInputStream bytes = new ByteArrayInputStream(original)) {
            final PDDocument unlocked = PDDocument.load(bytes);
            assertFalse(unlocked.isEncrypted());
        }

        final byte[] passwordProtected = new PdfBoxPasswordProtection().addProtection(original, "password".getBytes());

        try (ByteArrayInputStream bytes = new ByteArrayInputStream(passwordProtected)) {
            final PDDocument locked = PDDocument.load(bytes);
            assertTrue(locked.isEncrypted());
        }
    }

    @Test
    public void removeProtection() throws Exception {
        final byte[] original = TestResources.get(TestResources.get("/locked.pdf"));

        try (ByteArrayInputStream bytes = new ByteArrayInputStream(original)) {
            final PDDocument unlocked = PDDocument.load(bytes);
            assertTrue(unlocked.isEncrypted());
        }

        final byte[] unprotected = new PdfBoxPasswordProtection().removeProtection(original, "password".getBytes());

        try (ByteArrayInputStream bytes = new ByteArrayInputStream(unprotected)) {
            final PDDocument unlocked = PDDocument.load(bytes);
            assertFalse(unlocked.isEncrypted());
        }
    }
}