package com.bankingSystem.util;

import java.util.Random;

public class IdGenerator {

    private static final Random r = new Random();

    public static String generateAccountNumber(String name) {
        String initials = name.substring(0, 3).toUpperCase();
        int digits = 1000 + r.nextInt(9000);
        return initials + digits;
    }

    public static String generateTransactionId() {
        return "TXN-" + (100000 + r.nextInt(900000));
    }
}
