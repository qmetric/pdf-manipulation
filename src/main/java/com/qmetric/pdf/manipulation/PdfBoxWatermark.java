package com.qmetric.pdf.manipulation;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class PdfBoxWatermark implements Watermark {
    public final PDFToBytes pdfToBytes = new PDFToBytes();

    @Override
    public byte[] watermark(byte[] originalPdf, byte[] watermarkImage, byte[] password) throws RuntimeException {
        try {
            try (ByteArrayInputStream bytes = new ByteArrayInputStream(originalPdf)) {
                return process(watermarkImage, password, bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] process(byte[] watermarkImage, byte[] password, ByteArrayInputStream bytes) throws IOException, CryptographyException {
        try (final PDDocument document = PDDocument.load(bytes)) {

            if (document.isEncrypted()) {
                document.decrypt(new String(password));
                document.setAllSecurityToBeRemoved(true);
            }

            stampWatermark(document, ImageIO.read(new ByteArrayInputStream(watermarkImage)));

            return pdfToBytes.bytes(document);
        }
    }

    private void stampWatermark(PDDocument document, BufferedImage image) throws IOException {
        @SuppressWarnings("unchecked") final List<PDPage> pages = document.getDocumentCatalog().getAllPages();

        for (PDPage page : pages) {
            PDXObjectImage pdPixelMap = new PDPixelMap(document, image);
            try(PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true)) {
                contentStream.drawXObject(pdPixelMap, 0, 0, pdPixelMap.getWidth() * 1.0f, pdPixelMap.getHeight() * 1.0f);
            }
        }
    }
}
