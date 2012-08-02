package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.qmetric.document.watermark.DefaultPdfStamperFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;
import org.junit.Test;

import java.io.OutputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultPdfStamperFactoryTest
{
    private DefaultPdfStamperFactory defaultPdfStamperFactory = new DefaultPdfStamperFactory(OWNER_PASSWORD);

    private final PdfReader reader = mock(PdfReader.class);

    private final FileContent content = mock(FileContent.class);

    private static final String OWNER_PASSWORD = "";

    @Test
    public void showCreateNewStamperUsingSuppliedPdf()
    {
        final PdfStamper stamper = defaultPdfStamperFactory.newPdfStamper(reader, content);

        assertThat(stamper.getReader(), equalTo(reader));
    }

    @Test
    public void shouldSetEncryptionOptionsOnStamper() throws Exception
    {
        final OutputStream outputStream = mock(OutputStream.class);

        when(content.getOutputStream()).thenReturn(outputStream);
        when(reader.getPermissions()).thenReturn(1);
        when(reader.getCryptoMode()).thenReturn(2);

        when(reader.isEncrypted()).thenReturn(true);

        defaultPdfStamperFactory.newPdfStamper(reader, content);

        verify(reader).getCryptoMode();
    }

    @Test(expected = RuntimeException.class)
    public void wrapsExceptionsAsRuntimeException() throws Exception
    {
        //noinspection ThrowableInstanceNeverThrown
        when(content.getOutputStream()).thenThrow(new FileSystemException("exception"));

        defaultPdfStamperFactory.newPdfStamper(reader, content);
    }

    @Test(expected = RuntimeException.class)
    public void wrapsDocumentExceptionAsRuntimeException() throws Exception
    {
        final OutputStream outputStream = mock(OutputStream.class);
        when(content.getOutputStream()).thenReturn(outputStream);

        //noinspection ThrowableInstanceNeverThrown
        when(reader.getCryptoMode()).thenThrow(new com.itextpdf.text.DocumentException("doc exception"));

        defaultPdfStamperFactory.newPdfStamper(reader, content);
    }
}