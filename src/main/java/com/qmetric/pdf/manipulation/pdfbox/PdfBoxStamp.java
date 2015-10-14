package com.qmetric.pdf.manipulation.pdfbox;

import com.qmetric.pdf.helper.PDFAsBytes;
import com.qmetric.pdf.manipulation.PasswordProtection;
import com.qmetric.pdf.manipulation.Stamp;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class PdfBoxStamp implements Stamp {
    private final PDFAsBytes pdfAsBytes = new PDFAsBytes();

    private final PasswordProtection encryption = new PdfBoxPasswordProtection();

    @Override
    public byte[] stamp(byte[] documentAsBytes, String stamp, byte[] password) throws RuntimeException {
        try {
            final byte[] unlockedDocument = encryption.removeProtection(documentAsBytes, password);

            try (ByteArrayInputStream documentAsInputStream = new ByteArrayInputStream(unlockedDocument)) {
                final byte[] documentStamped = stampDocument(stamp, documentAsInputStream);
                return encryption.addProtection(documentStamped, password);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] stampDocument(String stamp, ByteArrayInputStream documentAsInputStream) throws IOException {
        try (final PDDocument document = PDDocument.load(documentAsInputStream)) {

            @SuppressWarnings("unchecked") List<PDPage> allPages = document.getDocumentCatalog().getAllPages();

            for (PDPage page : allPages) {
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20.0f);
                    contentStream.setNonStrokingColor(255, 0, 0);
                    contentStream.drawString(stamp);
                    contentStream.endText();
                }
            }

            return pdfAsBytes.valueOf(document);
        }
    }
}
