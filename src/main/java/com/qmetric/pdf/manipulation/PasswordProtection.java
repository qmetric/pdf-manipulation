package com.qmetric.pdf.manipulation;

public interface PasswordProtection {
    /**
     * Creates a new password protection pdf.
     *
     * @param originalPdf in bytes
     * @param password    in bytes
     * @return new pdf without password protection
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] removeProtection(byte[] originalPdf, byte[] password) throws RuntimeException;

    /**
     * Creates a new password protection pdf.
     *
     * @param originalPdf in bytes
     * @param password    in bytes
     * @return new pdf with password protection
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] addProtection(byte[] originalPdf, byte[] password) throws RuntimeException;
}
