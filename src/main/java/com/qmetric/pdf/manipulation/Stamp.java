package com.qmetric.pdf.manipulation;

public interface Stamp {
    /**
     * Adds a stamped message to all pages
     *
     * @param documentAsBytes as bytes
     * @param stamp           as String
     * @param password        as bytes
     * @return new pdf with stamp on every page
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] stamp(byte[] documentAsBytes, String stamp, byte[] password) throws RuntimeException;
}
