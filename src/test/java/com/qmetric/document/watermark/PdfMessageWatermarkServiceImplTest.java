package com.qmetric.document.watermark;

import com.qmetric.document.watermark.strategy.MessageWatermarkStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PdfMessageWatermarkServiceImplTest
{
    private MessageWatermarkStrategyFactory watermarkStrategyFactory = mock(MessageWatermarkStrategyFactory.class);

    private PdfWatermarkService pdfWatermarkService = mock(PdfWatermarkService.class);

    private final PdfMessageWatermarkServiceImpl watermarkService = new PdfMessageWatermarkServiceImpl(watermarkStrategyFactory, pdfWatermarkService);

    private final String message = "message";

    private final String sourcePath = "source";

    private final String outputPath = "output";

    private MessageWatermarkStrategy strategy = mock(MessageWatermarkStrategy.class);

    @Before
    public void setup() throws Exception
    {
        when(watermarkStrategyFactory.strategyFor(message)).thenReturn(strategy);
        
        watermarkService.watermark(sourcePath, outputPath, message);
    }

    @Test
    public void shouldCreateWatermarkStrategyForMessage()
    {
        verify(watermarkStrategyFactory).strategyFor(message);
    }

    @Test
    public void shouldCallWatermarkService() throws Exception
    {
        verify(pdfWatermarkService).watermark(sourcePath, outputPath, strategy);
    }
}
