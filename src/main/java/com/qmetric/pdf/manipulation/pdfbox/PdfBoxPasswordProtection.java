package com.qmetric.pdf.manipulation.pdfbox;

import com.qmetric.pdf.helper.PDFAsBytes;
import com.qmetric.pdf.manipulation.PasswordProtection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.ByteArrayInputStream;

public class PdfBoxPasswordProtection implements PasswordProtection {

    private final PDFAsBytes pdfAsBytes = new PDFAsBytes();

    @Override
    public byte[] removeProtection(byte[] documentAsBytes, byte[] password) throws RuntimeException {
        try (ByteArrayInputStream documentAsInputStream = new ByteArrayInputStream(documentAsBytes)) {
            try (final PDDocument document = PDDocument.load(documentAsInputStream)) {
                if (document.isEncrypted()) {
                    document.decrypt(new String(password));
                    document.setAllSecurityToBeRemoved(true);
                }

                return pdfAsBytes.valueOf(document);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] addProtection(byte[] documentAsBytes, byte[] password) throws RuntimeException {
        try (ByteArrayInputStream documentAsInputStream = new ByteArrayInputStream(documentAsBytes)) {
            try (final PDDocument document = PDDocument.load(documentAsInputStream)) {
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

                return pdfAsBytes.valueOf(document);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
