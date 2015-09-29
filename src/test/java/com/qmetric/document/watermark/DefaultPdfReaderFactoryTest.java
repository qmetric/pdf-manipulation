package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as
 * published by the Free Software Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY
 * PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 * <br/><br/>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program; if not, see http://www.gnu.org/licenses or write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/ <br/><br/>The interactive user interfaces in modified source and object code versions of this program must
 * display Appropriate Legal Notices, as required under Section 5 of the GNU Affero General Public License. <br/><br/>In accordance with Section 7(b)
 * of the GNU Affero General Public License, you must retain the producer line in every PDF that is created or manipulated using iText. <br/><br/>You
 * can be released from the requirements of the license by purchasing a commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the source code of your own applications. These activities include: offering
 * paid services to customers as an ASP, serving PDFs on the fly in a web application, shipping iText with a closed source product.
 */
public class DefaultPdfReaderFactoryTest
{
    static byte[] bytes;
    static {
        try {
            bytes = Files.readAllBytes(Paths.get(DefaultPdfReaderFactoryTest.class.getResource("/pdf/ExpectedDocument.pdf").toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DefaultPdfReaderFactory defaultPdfReaderFactory = new DefaultPdfReaderFactory("password");


    @Test
    public void createsPdfFromFileContent() throws Exception
    {
        final PdfReader pdfReader = defaultPdfReaderFactory.newPdfReader(bytes);

        assertThat(pdfReader.getFileLength(), equalTo(bytes.length));
    }
}
