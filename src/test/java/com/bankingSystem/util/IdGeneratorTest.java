package com.bankingSystem.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void testGenerateAccountNumber() {
        String id = IdGenerator.generateAccountNumber("John");
        assertTrue(id.startsWith("JOH"));
        assertEquals(7, id.length()); // 3 letters + 4 digits
    }

    @Test
    void testGenerateTransactionId() {
        String txn = IdGenerator.generateTransactionId();
        assertTrue(txn.startsWith("TXN-"));
    }
}
