package com.bankingSystem.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void testAccountModel() {
        Account acc = new Account("ACC01", "John");
        acc.setBalance(500.0);
        acc.getTransactionIds().add("TXN1");//because these may break serv lgc later

        assertEquals("ACC01", acc.getAccountNumber());
        assertEquals("John", acc.getHolderName());
        assertEquals(500.0, acc.getBalance());
        assertTrue(acc.getTransactionIds().contains("TXN1"));
    }

    @Test
    void testTransactionModel() {
        Transaction t = new Transaction("T1", "DEPOSIT", 100.0,
                "SUCCESS", "A1", "A2");

        assertEquals("T1", t.getTransactionId());
        assertEquals("DEPOSIT", t.getType());
        assertEquals(100.0, t.getAmount());
        assertEquals("A1", t.getSourceAccount());
        assertEquals("A2", t.getDestinationAccount());
        t.setId("ID123");
        assertEquals("ID123", t.getId());
        t.setTransactionId("T999");
        assertEquals("T999", t.getTransactionId());
        t.setType("WITHDRAW");
        assertEquals("WITHDRAW", t.getType());
        t.setAmount(250.0);
        assertEquals(250.0, t.getAmount());
        t.setStatus("FAILED");
        assertEquals("FAILED", t.getStatus());
        t.setSourceAccount("SRC1234");
        assertEquals("SRC1234", t.getSourceAccount());
        t.setDestinationAccount("DST5678");
        assertEquals("DST5678", t.getDestinationAccount());
        Instant newTime = Instant.now();
        t.setTimestamp(newTime);
        assertEquals(newTime, t.getTimestamp());

    }
}
