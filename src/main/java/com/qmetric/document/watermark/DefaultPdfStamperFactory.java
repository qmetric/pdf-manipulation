package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.vfs.FileContent;

import java.io.IOException;

public class DefaultPdfStamperFactory implements PdfStamperFactory
{
    private final byte[] ownerPassword;

    public DefaultPdfStamperFactory(final String passwordString)
    {
        this.ownerPassword = passwordString.getBytes();
    }

    @Override public PdfStamper newPdfStamper(final PdfReader pdfReader, final FileContent fileContent)
    {
        try
        {
            final PdfStamper pdf = new PdfStamper(pdfReader, fileContent.getOutputStream());

            if (pdfReader.isEncrypted())
            {
                pdf.setEncryption(null, ownerPassword, pdfReader.getPermissions(), pdfReader.getCryptoMode());
            }

            return pdf;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        catch (com.itextpdf.text.DocumentException e)
        {
            throw new RuntimeException(e);
        }
    }
}