package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import com.qmetric.document.watermark.DefaultPdfReaderFactory;
import com.qmetric.utilities.file.FileUtils;
import org.apache.commons.vfs.FileContent;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPdfReaderFactoryTest
{
    private DefaultPdfReaderFactory defaultPdfReaderFactory = new DefaultPdfReaderFactory("password");

    private FileContent pdfFile = mock(FileContent.class);

    private FileContent content;

    @Before
    public void setup() throws Exception
    {
        content = new FileUtils().resolveFile("res:pdf/ExpectedDocument.pdf").getContent();
        when(pdfFile.getInputStream()).thenReturn(content.getInputStream());
    }

    @Test
    public void createsPdfFromFileContent() throws Exception
    {
        final PdfReader pdfReader = defaultPdfReaderFactory.newPdfReader(pdfFile);

        assertThat((long) pdfReader.getFileLength(), equalTo(content.getSize()));
    }
}
