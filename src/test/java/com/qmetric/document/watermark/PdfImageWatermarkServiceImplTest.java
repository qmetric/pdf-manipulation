package com.qmetric.document.watermark;

import com.qmetric.document.watermark.strategy.ImageWatermarkStrategy;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PdfImageWatermarkServiceImplTest
{
    private ImageWatermarkStrategy watermarkStrategy = mock(ImageWatermarkStrategy.class);

    private PdfWatermarkService pdfWatermarkService = mock(PdfWatermarkService.class);

    private final PdfImageWatermarkServiceImpl watermarkService = new PdfImageWatermarkServiceImpl(watermarkStrategy, pdfWatermarkService);
    
    @Test
    public void shouldCallWatermarkServiceWithStrategy() throws Exception
    {
        final String sourcePath = "sourcePath";
        final String outputPath = "outputPath";

        watermarkService.watermark(sourcePath, outputPath);
        
        verify(pdfWatermarkService).watermark(sourcePath, outputPath, watermarkStrategy);
    }
}
