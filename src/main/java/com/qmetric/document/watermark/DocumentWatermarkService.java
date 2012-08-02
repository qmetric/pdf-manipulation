package com.qmetric.document.watermark;

public interface DocumentWatermarkService
{
    void watermark(String documentSourcePath, String documentOutputPath) throws Exception;
}