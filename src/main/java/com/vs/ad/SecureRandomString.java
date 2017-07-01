package com.vs.ad;

import java.security.SecureRandom;

public class SecureRandomString {
    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            tmp.append(ch);
        }
        // Add some symbols
        tmp.append("()`~!@#$%^&*-+=|{}[]:;\"'<>,.?/");
        symbols = tmp.toString().toCharArray();
    }

    public static String get(int length) {
        SecureRandomString srs = new SecureRandomString(length);
        return srs.nextString();
    }


    private final SecureRandom random = new SecureRandom();

    private final char[] buf;

    public SecureRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        buf = new char[length];

    }


    public String nextString() {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

}
