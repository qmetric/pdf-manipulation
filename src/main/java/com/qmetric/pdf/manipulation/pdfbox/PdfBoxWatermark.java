package com.qmetric.pdf.manipulation.pdfbox;

import com.qmetric.pdf.helper.PDFAsBytes;
import com.qmetric.pdf.manipulation.PasswordProtection;
import com.qmetric.pdf.manipulation.Watermark;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class PdfBoxWatermark implements Watermark {
    private final PDFAsBytes pdfAsBytes = new PDFAsBytes();

    private final PasswordProtection encryption = new PdfBoxPasswordProtection();

    @Override
    public byte[] watermark(byte[] documentAsBytes, byte[] watermarkImage, byte[] password) throws RuntimeException {
        try {
            final byte[] unlockedDocument = encryption.removeProtection(documentAsBytes, password);

            try (ByteArrayInputStream documentAsInputStream = new ByteArrayInputStream(unlockedDocument)) {
                return encryption.addProtection(addWatermark(watermarkImage, documentAsInputStream), password);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] addWatermark(byte[] watermarkImage, ByteArrayInputStream documentAsInputStream) throws IOException, CryptographyException {
        try (final PDDocument document = PDDocument.load(documentAsInputStream)) {

            @SuppressWarnings("unchecked") final List<PDPage> pages = document.getDocumentCatalog().getAllPages();

            for (PDPage page : pages) {
                PDXObjectImage pdPixelMap = new PDPixelMap(document, ImageIO.read(new ByteArrayInputStream(watermarkImage)));
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true)) {
                    contentStream.drawXObject(pdPixelMap, 0, 0, pdPixelMap.getWidth() * 1.0f, pdPixelMap.getHeight() * 1.0f);
                }
            }

            return pdfAsBytes.valueOf(document);
        }
    }
}
