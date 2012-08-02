package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.vfs.FileContent;

import java.io.IOException;

public class DefaultPdfReaderFactory implements PdfReaderFactory
{
    private final byte[] ownerPassword;

    public DefaultPdfReaderFactory(final String passwordString)
    {
        this.ownerPassword = passwordString.getBytes();
    }

    @Override public PdfReader newPdfReader(final FileContent fileContent) throws IOException
    {
        return new PdfReader(fileContent.getInputStream(), ownerPassword);
    }
}
