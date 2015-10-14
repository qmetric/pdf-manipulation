package com.qmetric.pdf.manipulation;

public interface PasswordProtection {
    /**
     * Creates a new password protection pdf.
     *
     * @param documentAsBytes as bytes
     * @param password        as bytes
     * @return new pdf without password protection
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] removeProtection(byte[] documentAsBytes, byte[] password) throws RuntimeException;

    /**
     * Creates a new password protection pdf.
     *
     * @param documentAsBytes as bytes
     * @param password        as bytes
     * @return new pdf with password protection
     * @throws RuntimeException if anything unexpected happens
     */
    byte[] addProtection(byte[] documentAsBytes, byte[] password) throws RuntimeException;
}
