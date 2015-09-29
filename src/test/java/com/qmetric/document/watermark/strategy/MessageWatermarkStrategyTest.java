package com.qmetric.document.watermark.strategy;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as published by the Free Software
 * Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with this program; if not, see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/ The interactive user
 * interfaces in modified source and object code versions of this program must display Appropriate Legal Notices, as required under Section 5 of the GNU Affero General Public
 * License. In accordance with Section 7(b) of the GNU Affero General Public License, you must retain the producer line in every PDF that is created or manipulated using
 * iText. You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the source code of your own applications. These activities include: offering paid services to customers as
 * an ASP, serving PDFs on the fly in a web application, shipping iText with a closed source product.
 */

public class MessageWatermarkStrategyTest
{
    private static final String DATE_STAMP = "10 Nov 2011";

    private final MessageWatermarkStrategy watermarkStrategy = new MessageWatermarkStrategy(DATE_STAMP);

    private PdfContentByte contentPageOne;

    private PdfContentByte contentPageTwo;
    
    private ArgumentCaptor<PdfGState> transparencyArgumentCaptor;

    private InOrder order;

    @Before
    public void setup() throws Exception
    {
        final PdfReader reader = mock(PdfReader.class);
        final PdfStamper outputPdf = mock(PdfStamper.class);

        configureTwoPages(reader, outputPdf);

        transparencyArgumentCaptor = ArgumentCaptor.forClass(PdfGState.class);

        order = inOrder(contentPageOne);

        watermarkStrategy.apply(reader, outputPdf);
    }

    @Test
    public void shouldApplyTextOpacity()
    {
        order.verify(contentPageOne).saveState();
        order.verify(contentPageOne).setGState(transparencyArgumentCaptor.capture());

        final PdfGState pdfGState = transparencyArgumentCaptor.getValue();

        final PdfGState expectedState = new PdfGState();
        expectedState.setFillOpacity(0.6f);
        expectedState.setStrokeOpacity(0.6f);

        for(PdfName pdfName : pdfGState.getKeys())
        {
            assertThat(pdfGState.get(pdfName).toString(), equalTo(expectedState.get(pdfName).toString()));
        }
    }

    @Test
    public void shouldAddTextInBottomLeftCornerInSize14RedFont()
    {
        order.verify(contentPageOne).beginText();
        order.verify(contentPageOne).setFontAndSize(Mockito.any(BaseFont.class), eq(14f));
        order.verify(contentPageOne).setRGBColorFill(255, 0, 0);
        order.verify(contentPageOne).showTextAligned(PdfContentByte.ALIGN_LEFT, DATE_STAMP, 2f, 10f, 0);
        order.verify(contentPageOne).endText();
        order.verify(contentPageOne).restoreState();
    }

    @Test
    public void shouldAddTextToEachPage()
    {
        verify(contentPageOne).restoreState();
        verify(contentPageTwo).restoreState();
    }

    private void configureTwoPages(final PdfReader reader, final PdfStamper outputPdf)
    {
        when(reader.getNumberOfPages()).thenReturn(2);

        final Rectangle pageSize = mock(Rectangle.class);

        when(pageSize.getWidth()).thenReturn(64f);
        when(pageSize.getHeight()).thenReturn(80f);

        when(reader.getPageSizeWithRotation(Mockito.anyInt())).thenReturn(pageSize);

        pageOne(outputPdf);
        pageTwo(outputPdf);
    }

    private void pageTwo(final PdfStamper outputPdf)
    {
        contentPageTwo = mock(PdfContentByte.class);
        when(outputPdf.getOverContent(2)).thenReturn(contentPageTwo);
    }

    private void pageOne(final PdfStamper outputPdf)
    {
        contentPageOne = mock(PdfContentByte.class);
        when(outputPdf.getOverContent(1)).thenReturn(contentPageOne);
    }
}
