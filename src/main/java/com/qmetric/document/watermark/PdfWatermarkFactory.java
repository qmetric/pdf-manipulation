package com.qmetric.document.watermark;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
public class PdfWatermarkFactory {
    private final Path imagePath;

    public PdfWatermarkFactory(final String imagePath) {
        this.imagePath = Paths.get(imagePath);
    }

    public Image createImage(final PdfReader reader) {
        final Rectangle pageSize = reader.getPageSize(1);

        try {
            final Image image = Image.getInstance(Files.readAllBytes(imagePath));

            image.setAbsolutePosition(getXOffset(pageSize, image), getYOffset(pageSize, image));

            return image;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private float getXOffset(final Rectangle pageSize, final Image image) {
        return (float) Math.round(pageSize.getWidth() - image.getWidth()) / 2;
    }

    private float getYOffset(final Rectangle pageSize, final Image image) {
        return (float) Math.round(pageSize.getHeight() - image.getHeight()) / 2;
    }
}
