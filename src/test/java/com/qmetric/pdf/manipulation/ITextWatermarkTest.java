package com.qmetric.pdf.manipulation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Test;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;

public class ITextWatermarkTest {

    final byte[] password = "test".getBytes();
    final byte[] watermark = TestResources.get(TestResources.get("/watermark-image.png"));
    final byte[] originalPdf = TestResources.get(TestResources.get("/locked.pdf"));
    final byte[] expectedPage1WithWatermark = TestResources.get(TestResources.get("/1.jpg"));

    @Test
    public void shouldAddWatermarkImage() throws Exception {

        final byte[] watermarkedPdf = new ITextWatermark().watermark(originalPdf, watermark, password);

        try (PDDocument pdDocument = PDDocument.load(new ByteArrayInputStream(watermarkedPdf))) {

            pdDocument.decrypt(new String(password));

            byte[] page1AsImage = getPageAsImage((PDPage) pdDocument.getDocumentCatalog().getAllPages().get(0));

            System.out.println(page1AsImage.length);
            System.out.println(expectedPage1WithWatermark.length);

            assertTrue(Arrays.equals(page1AsImage, expectedPage1WithWatermark));
        }
    }

    private byte[] getPageAsImage(PDPage page) throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            BufferedImage image = page.convertToImage();

            ImageWriter imageWriter = null;

            Iterator iterator = javax.imageio.ImageIO.getImageWritersByMIMEType("image/jpeg");
            if (iterator.hasNext()) {
                imageWriter = (ImageWriter) iterator.next();
            }

            ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(output);
            if (imageWriter != null) {
                imageWriter.setOutput(ios);
                imageWriter.write(new IIOImage(image, null, null));
                ios.flush();
                imageWriter.dispose();
            }

            return output.toByteArray();
        }
    }
}


