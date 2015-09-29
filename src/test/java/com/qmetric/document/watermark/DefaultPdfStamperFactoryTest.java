package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DefaultPdfStamperFactoryTest
{
    private static final String OWNER_PASSWORD = "test";

    private DefaultPdfStamperFactory defaultPdfStamperFactory = new DefaultPdfStamperFactory(OWNER_PASSWORD);

    private Path destination = Paths.get("../../target");

    private PdfReader reader;

    @Before
    public void context() throws Exception {
        reader = new PdfReader(getClass().getResource("/pdf/ExpectedDocument.pdf"));
    }

    @Test
    public void showCreateNewStamperUsingSuppliedPdf() throws Exception
    {
        final PdfStamper stamper = defaultPdfStamperFactory.newPdfStamper(reader, destination);

        assertThat(stamper.getReader(), equalTo(reader));
    }

    @Test
    public void shouldSetNoEncryptionOptionsOnStamper() throws Exception
    {
        reader = new PdfReader(getClass().getResource("/pdf/ExpectedDocument.pdf"), OWNER_PASSWORD.getBytes());
        defaultPdfStamperFactory.newPdfStamper(reader, destination);

        final int cryptoMode = reader.getCryptoMode();

        assertThat(cryptoMode, equalTo(-1));
    }

    @Test
    public void shouldSetEncryptionOptionsOnStamper() throws Exception
    {
        reader = new PdfReader(getClass().getResource("/pdf/pdf-example-password.original.pdf"), OWNER_PASSWORD.getBytes());
        defaultPdfStamperFactory.newPdfStamper(reader, destination);

        final int cryptoMode = reader.getCryptoMode();

        assertThat(cryptoMode, equalTo(1));
    }
}