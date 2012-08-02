package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.qmetric.utilities.file.FileUtils;
import org.apache.commons.vfs.FileObject;

import java.io.IOException;

public class DocumentWatermarkServiceImpl implements DocumentWatermarkService
{
    private final PdfReaderFactory pdfReaderFactory;

    private final PdfStamperFactory pdfStamperFactory;

    private final PdfWatermarkFactory pdfWatermarkFactory;

    private final FileUtils fileUtils;

    public DocumentWatermarkServiceImpl(final PdfReaderFactory pdfReaderFactory, final PdfStamperFactory pdfStamperFactory,
                                        final PdfWatermarkFactory pdfWatermarkFactory, final FileUtils fileUtils)
    {
        this.pdfReaderFactory = pdfReaderFactory;
        this.pdfStamperFactory = pdfStamperFactory;
        this.pdfWatermarkFactory = pdfWatermarkFactory;
        this.fileUtils = fileUtils;
    }

    @Override public void watermark(final String documentSourcePath, final String documentOutputPath) throws Exception
    {
        // source reader
        final FileObject sourcePdf = fileUtils.resolveFile(documentSourcePath);

        final PdfReader reader;
        try
        {
            reader = pdfReaderFactory.newPdfReader(sourcePdf.getContent());
        }
        catch (final IOException e)
        {
            throw new RuntimeException(String.format("Failed to create new pdf reader because pdf password was incorrect. documentSourcePath[%s], " +
                                                     "documentOutputPath[%s]", documentSourcePath, documentOutputPath), e);
        }

        // create a stamper that will copy the document to a new file\
        FileObject outputFile = null;
        PdfStamper outputPdf = null;

        try
        {
            outputFile = fileUtils.resolveFile(documentOutputPath);

            outputPdf = pdfStamperFactory.newPdfStamper(reader, outputFile.getContent());

            Image image = pdfWatermarkFactory.createImage(reader);

            int pageNumber = 1;
            while (pageNumber <= reader.getNumberOfPages())
            {
                // watermark page
                outputPdf.getOverContent(pageNumber).addImage(image);

                pageNumber++;
            }
        }
        finally
        {
            cleanUp(sourcePdf, outputFile, outputPdf);
        }
    }

    private void cleanUp(final FileObject sourcePdf, final FileObject outputFile, final PdfStamper outputPdf)
            throws com.itextpdf.text.DocumentException, IOException
    {
        if (outputPdf != null)
        {
            outputPdf.close();
        }

        fileUtils.closeQuietly(outputFile);
        fileUtils.closeQuietly(sourcePdf);
    }
}
