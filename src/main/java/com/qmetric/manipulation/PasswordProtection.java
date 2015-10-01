package com.qmetric.manipulation;

public interface PasswordProtection {

    byte[] removeProtection(byte[] originalPdf, byte[] password);

    byte[] addProtection(byte[] originalPdf, byte[] password);
}
