package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.qmetric.utilities.file.FileUtils;
import com.qmetric.utilities.io.ImageUtils;

public class PdfWatermarkFactory
{
    private final FileUtils fileUtils;

    private final ImageUtils imageUtils;

    private final String imagePath;

    public PdfWatermarkFactory(final FileUtils fileUtils, final ImageUtils imageUtils, final String imagePath)
    {
        this.fileUtils = fileUtils;
        this.imageUtils = imageUtils;
        this.imagePath = imagePath;
    }

    public Image createImage(final PdfReader reader)
    {
        final Rectangle pageSize = reader.getPageSize(1);

        try
        {
            final Image image = imageUtils.getInstance(fileUtils.bytesFrom(imagePath));

            image.setAbsolutePosition(getXOffset(pageSize, image), getYOffset(pageSize, image));

            return image;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private float getXOffset(final Rectangle pageSize, final Image image)
    {
        return (float) Math.round(pageSize.getWidth() - image.getWidth()) / 2;
    }

    private float getYOffset(final Rectangle pageSize, final Image image)
    {
        return (float) Math.round(pageSize.getHeight() - image.getHeight()) / 2;
    }
}
