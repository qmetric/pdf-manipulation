package com.qmetric.document.watermark;

import com.qmetric.document.watermark.strategy.MessageWatermarkStrategy;

public class MessageWatermarkStrategyFactory
{
    public MessageWatermarkStrategy strategyFor(final String watermarkText)
    {
        return new MessageWatermarkStrategy(watermarkText);
    }
}
