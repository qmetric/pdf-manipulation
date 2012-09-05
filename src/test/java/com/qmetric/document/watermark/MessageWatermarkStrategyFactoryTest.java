package com.qmetric.document.watermark;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MessageWatermarkStrategyFactoryTest
{
    @Test
    public void shouldCreateMessageWatermarkStrategy()
    {
        final String message = "message";

        assertNotNull(new MessageWatermarkStrategyFactory().strategyFor(message));
    }
}
