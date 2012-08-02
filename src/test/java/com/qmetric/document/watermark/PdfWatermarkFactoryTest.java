package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.qmetric.utilities.file.FileUtils;
import com.qmetric.utilities.io.ImageUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PdfWatermarkFactoryTest
{
    private static final String IMAGE_PATH = "imagePath";

    private PdfReader pdfReader;

    private PdfWatermarkFactory pdfWatermarkFactory;

    private ImageUtils imageUtils = mock(ImageUtils.class);

    private FileUtils fileUtils = mock(FileUtils.class);

    @Before
    public void context()
    {
        pdfReader = definePdfReader();

        pdfWatermarkFactory = new PdfWatermarkFactory(fileUtils, imageUtils, IMAGE_PATH);
    }

    @Test
    public void shouldReturnImageWithExpectedPosition() throws Exception
    {
        final Image image = mock(Image.class);
        when(imageUtils.getInstance(Mockito.<byte[]>any())).thenReturn(image);

        final Image watermark = pdfWatermarkFactory.createImage(pdfReader);

        assertThat(watermark, equalTo(image));
        verify(fileUtils).bytesFrom(IMAGE_PATH);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOnError() throws Exception
    {
        final PdfReader pdfReader = definePdfReader();
        //noinspection ThrowableInstanceNeverThrown
        when(imageUtils.getInstance(Mockito.<byte[]>any())).thenThrow(new IOException());

        pdfWatermarkFactory.createImage(pdfReader);
    }

    private PdfReader definePdfReader()
    {
        final PdfReader pdfReader = mock(PdfReader.class);
        final Rectangle pageSize = mock(Rectangle.class);

        when(pageSize.getWidth()).thenReturn(50f);
        when(pageSize.getHeight()).thenReturn(50f);
        when(pdfReader.getPageSize(1)).thenReturn(pageSize);

        return pdfReader;
    }
}