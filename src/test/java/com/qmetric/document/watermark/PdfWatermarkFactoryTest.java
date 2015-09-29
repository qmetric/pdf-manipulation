package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PdfWatermarkFactoryTest
{
    public static final URL IMAGE_URL = PdfWatermarkFactoryTest.class.getResource("/image.jpg");

    private static final String IMAGE_PATH = IMAGE_URL.getPath();

    static byte[] IMAGE_BYTES;

    static {
        try {
            IMAGE_BYTES = Files.readAllBytes(Paths.get(IMAGE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfReader pdfReader;

    private PdfWatermarkFactory pdfWatermarkFactory;

    @Before
    public void context() throws Exception {
        pdfReader = new PdfReader(PdfWatermarkFactoryTest.class.getResource("/pdf/ExpectedDocument.pdf"));

        pdfWatermarkFactory = new PdfWatermarkFactory(IMAGE_PATH);
    }

    @Test
    public void shouldReturnImageWithExpectedPosition() throws Exception
    {
        final Image watermark = pdfWatermarkFactory.createImage(pdfReader);

        assertThat(watermark.getAbsoluteX(), equalTo(-42.5F));
        assertThat(watermark.getAbsoluteY(), equalTo(10.0F));
    }
}