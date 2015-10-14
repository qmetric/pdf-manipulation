package com.qmetric.pdf.manipulation;

public interface Watermark {
    /**
     * Adds a watermark image to all pages
     *
     * @param documentAsBytes as bytes
     * @param watermarkImage  as bytes
     * @param password        as bytes
     * @return new pdf with watermark image on every page
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] watermark(byte[] documentAsBytes, byte[] watermarkImage, byte[] password) throws RuntimeException;
}
