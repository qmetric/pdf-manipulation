package com.qmetric.pdf.manipulation;

public interface Watermark {
    /**
     * Adds a watermark image to all pages
     *
     * @param originalPdf in bytes
     * @param watermarkImage in bytes
     * @param password in bytes
     * @return new pdf with watermark image on every page
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] watermark(byte[] originalPdf, byte[] watermarkImage, byte[] password) throws RuntimeException;
}
