package com.qmetric.pdf.manipulation;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;

public class PDFToBytes {
    public byte[] bytes(PDDocument document) throws RuntimeException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            document.save(result);
            return result.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
