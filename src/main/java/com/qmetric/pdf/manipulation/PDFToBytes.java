package com.qmetric.pdf.manipulation;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFToBytes {
    public byte[] bytes(PDDocument document) throws IOException, COSVisitorException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            document.save(result);
            return result.toByteArray();
        }
    }
}
