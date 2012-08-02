package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.qmetric.utilities.file.FileUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentWatermarkServiceImplTest
{
    static final String DOCUMENT_SOURCE_PATH = "documentSourcePath";

    static final String DOCUMENT_OUTPUT_PATH = "documentOutputPath";

    private PdfReaderFactory pdfReaderFactory;

    private PdfStamperFactory pdfStamperFactory;

    private PdfWatermarkFactory pdfWatermarkFactory;

    private FileUtils fileUtils;

    private DocumentWatermarkService documentWatermarkService;

    @Before
    public void context()
    {
        pdfReaderFactory = mock(PdfReaderFactory.class);
        pdfStamperFactory = mock(PdfStamperFactory.class);
        pdfWatermarkFactory = mock(PdfWatermarkFactory.class);
        fileUtils = mock(FileUtils.class);

        documentWatermarkService = new DocumentWatermarkServiceImpl(pdfReaderFactory, pdfStamperFactory, pdfWatermarkFactory, fileUtils);
    }

    @Test
    public void shouldAddWatermarkImageToEveryPageInSourceDocument() throws Exception
    {
        final FileObject sourcePdf = mock(FileObject.class);
        final FileContent sourceFileContent = mock(FileContent.class);
        when(sourcePdf.getContent()).thenReturn(sourceFileContent);

        when(fileUtils.resolveFile(DOCUMENT_SOURCE_PATH)).thenReturn(sourcePdf);

        final PdfReader reader = mock(PdfReader.class);
        when(reader.getNumberOfPages()).thenReturn(2);

        when(pdfReaderFactory.newPdfReader(sourceFileContent)).thenReturn(reader);

        final FileObject outputFile = mock(FileObject.class);
        final FileContent outputFileContent = mock(FileContent.class);
        when(outputFile.getContent()).thenReturn(outputFileContent);

        when(fileUtils.resolveFile(DOCUMENT_OUTPUT_PATH)).thenReturn(outputFile);

        final PdfStamper outputPdf = mock(PdfStamper.class);
        final PdfContentByte page1Content = mock(PdfContentByte.class);
        final PdfContentByte page2Content = mock(PdfContentByte.class);
        when(outputPdf.getOverContent(1)).thenReturn(page1Content);
        when(outputPdf.getOverContent(2)).thenReturn(page2Content);
        when(pdfStamperFactory.newPdfStamper(reader, outputFileContent)).thenReturn(outputPdf);

        final Image watermark = mock(Image.class);
        when(pdfWatermarkFactory.createImage(reader)).thenReturn(watermark);

        documentWatermarkService.watermark(DOCUMENT_SOURCE_PATH, DOCUMENT_OUTPUT_PATH);

        Mockito.verify(page1Content).addImage(watermark);
        Mockito.verify(page2Content).addImage(watermark);

        Mockito.verify(outputPdf).close();

        Mockito.verify(fileUtils).closeQuietly(outputFile);
        Mockito.verify(fileUtils).closeQuietly(sourcePdf);
    }
}
