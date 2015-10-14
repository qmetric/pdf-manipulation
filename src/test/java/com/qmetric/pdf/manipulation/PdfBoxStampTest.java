package com.qmetric.pdf.manipulation;

import com.qmetric.pdf.manipulation.pdfbox.PdfBoxStamp;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertThat;

public class PdfBoxStampTest {

    @Test
    public void shouldAddStampToPdfPages() throws Exception {
        final byte[] original = TestResources.get(TestResources.get("/locked.pdf"));

        final String stampMessage = "Stamp message";

        final byte[] stamp = new PdfBoxStamp().stamp(original, stampMessage, "password".getBytes());

        final PDDocument stampedDocument = PDDocument.load(new ByteArrayInputStream(stamp));
        stampedDocument.decrypt("password");
        String allTextInDocument = new PDFTextStripper().getText(stampedDocument);

        assertThat(allTextInDocument, StringContains.containsString(stampMessage));
    }
}