package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.vfs.FileContent;

public interface PdfStamperFactory
{
    PdfStamper newPdfStamper(PdfReader pdfReader, FileContent fileContent);
}
