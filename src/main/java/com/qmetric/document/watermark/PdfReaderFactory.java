package com.qmetric.document.watermark;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.vfs.FileContent;

import java.io.IOException;

public interface PdfReaderFactory
{
    PdfReader newPdfReader(FileContent sourceFile) throws IOException;
}
