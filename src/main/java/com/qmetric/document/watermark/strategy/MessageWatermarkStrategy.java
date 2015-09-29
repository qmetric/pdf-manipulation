package com.qmetric.document.watermark.strategy;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.IOException;

/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as
 * published by the Free Software Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY
 * PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program; if not, see http://www.gnu.org/licenses or write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/ The interactive user interfaces in modified source and object code versions of this program must
 * display Appropriate Legal Notices, as required under Section 5 of the GNU Affero General Public License. In accordance with Section 7(b)
 * of the GNU Affero General Public License, you must retain the producer line in every PDF that is created or manipulated using iText. You
 * can be released from the requirements of the license by purchasing a commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the source code of your own applications. These activities include: offering
 * paid services to customers as an ASP, serving PDFs on the fly in a web application, shipping iText with a closed source product.
 */

public class MessageWatermarkStrategy implements PdfWatermarkStrategy
{
    private static final int NO_ROTATION = 0;

    private static final float OPACITY = 0.6f;

    private static final int RED = 255;

    private final String watermarkText;

    public MessageWatermarkStrategy(final String watermarkText)
    {
        this.watermarkText = watermarkText;
    }

    @Override
    public void apply(final PdfReader reader, final PdfStamper outputPdf) throws Exception
    {
        for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++)
        {
            final PdfContentByte overContent = outputPdf.getOverContent(pageNumber);

            applyTextTransparency(overContent);

            addTextToPage(reader.getPageSizeWithRotation(pageNumber), overContent, watermarkText);

            overContent.restoreState();
        }
    }

    private void addTextToPage(final Rectangle page, final PdfContentByte overContent, final String watermarkText) throws DocumentException, IOException
    {
        // Add text - adapted from example found at
        // http://footheory.com/blogs/donnfelker/archive/2008/05/11/using-itextsharp-to-watermark-write-text-to-existing-pdf-s.aspx
        overContent.beginText();

        final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, false);
        overContent.setFontAndSize(baseFont, 14);
        overContent.setRGBColorFill(RED, 0, 0);

        overContent.showTextAligned(PdfContentByte.ALIGN_LEFT, watermarkText, xAxisPosition(page), yAxisPosition(page), NO_ROTATION);
        overContent.endText();
    }

    private float yAxisPosition(final Rectangle pageSize)
    {
        return pageSize.getHeight() / 8;
    }

    private float xAxisPosition(final Rectangle pageSize)
    {
        return pageSize.getWidth() / 32;
    }

    private void applyTextTransparency(final PdfContentByte overContent)
    {
        // Make transparent - see http://itext-general.2136553.n4.nabble.com/Insert-transparent-Text-td2158904.html
        final PdfGState gstate = new PdfGState();
        gstate.setFillOpacity(OPACITY);
        gstate.setStrokeOpacity(OPACITY);

        overContent.saveState();
        overContent.setGState(gstate);
    }
}
