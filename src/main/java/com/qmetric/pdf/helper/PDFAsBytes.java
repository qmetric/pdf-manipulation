package com.qmetric.pdf.helper;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;

public class PDFAsBytes {
    public byte[] valueOf(PDDocument document) throws RuntimeException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            document.save(result);
            return result.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
