package com.qmetric.pdf.manipulation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.ByteArrayInputStream;

public class PdfBoxPasswordProtection implements PasswordProtection {

    public final PDFToBytes pdfToBytes = new PDFToBytes();

    @Override
    public byte[] removeProtection(byte[] originalPdf, byte[] password) throws RuntimeException {
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(originalPdf)) {
            try (final PDDocument document = PDDocument.load(bytes)) {
                if (document.isEncrypted()) {
                    document.decrypt(new String(password));
                    document.setAllSecurityToBeRemoved(true);
                }

                return pdfToBytes.bytes(document);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] addProtection(byte[] originalPdf, byte[] password) throws RuntimeException {
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(originalPdf)) {
            try (final PDDocument document = PDDocument.load(bytes)) {
                if (!document.isEncrypted()) {
                    AccessPermission ap = new AccessPermission();

                    ap.setReadOnly();
                    ap.setCanModify(false);
                    ap.setCanModifyAnnotations(false);

                    StandardProtectionPolicy spp = new StandardProtectionPolicy(new String(password), "", ap);

                    spp.setEncryptionKeyLength(128);
                    spp.setPermissions(ap);
                    document.protect(spp);
                }

                return pdfToBytes.bytes(document);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
