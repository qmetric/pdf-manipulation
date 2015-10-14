package com.qmetric.pdf.manipulation;

import com.qmetric.pdf.manipulation.pdfbox.PdfBoxWatermark;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class PdfBoxWatermarkTest {

    private static final Integer NUMBER_OF_PAGES = 3;

    @Test
    public void shouldAddWatermarkToAllThreePages() throws Exception {
        final byte[] pdf = TestResources.get(TestResources.get("/unlocked.pdf"));

        final byte[] image = TestResources.get(TestResources.get("/watermark-image.png"));

        final byte[] watermark = new PdfBoxWatermark().watermark(pdf, image, "password".getBytes());

        assertThat(countPNGImages(watermark), equalTo(NUMBER_OF_PAGES)); // very crude
    }


    private int countPNGImages(byte[] bytes) throws Exception {
        int i = 0;

        try (final PDDocument document = PDDocument.load(new ByteArrayInputStream(bytes))) {

            @SuppressWarnings("unchecked") List<PDPage> list = document.getDocumentCatalog().getAllPages();

            for (PDPage page : list) {
                PDResources pdResources = page.getResources();

                Map<String, PDXObject> pageImages = pdResources.getXObjects();


                for (PDXObject pdxObject : pageImages.values()) {
                    if (pdxObject instanceof PDXObjectImage && ((PDXObjectImage) pdxObject).getSuffix().equalsIgnoreCase("png")) {
                        i++;
                    }
                }
            }
        }

        return i;
    }
}