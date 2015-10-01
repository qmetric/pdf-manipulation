package com.qmetric.pdf.manipulation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class TestResources {

    static Path get(String x) {
        try {
            return Paths.get(TestResources.class.getResource(x).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static byte[] get(Path x) {
        try {
            return Files.readAllBytes(x);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        createPdf("/tmp/unlocked.pdf");
    }

    public static void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
//        writer.setEncryption("test".getBytes(), "test".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
        writer.createXmpMetadata();
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        document.add(new Paragraph("Hello World"));
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
    }
}
