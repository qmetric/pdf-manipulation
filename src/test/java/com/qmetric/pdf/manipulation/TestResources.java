package com.qmetric.pdf.manipulation;

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
}
