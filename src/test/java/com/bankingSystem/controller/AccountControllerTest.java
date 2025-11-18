package com.bankingSystem.controller;

import com.bankingSystem.dto.AmountRequest;
import com.bankingSystem.dto.CreateAccountRequest;
import com.bankingSystem.dto.TransferRequest;
import com.bankingSystem.model.Account;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Account acc = new Account("ACC001", "John");
        when(service.createAccount("John")).thenReturn(acc);

        CreateAccountRequest req = new CreateAccountRequest();
        req.setHolderName("John");

        ResponseEntity<Account> res = controller.create(req);

        assertEquals(201, res.getStatusCodeValue());
        assertEquals("John", res.getBody().getHolderName());
    }

    @Test
    void testGet() {
        Account acc = new Account("ACC001", "John");
        when(service.getAccount("ACC001")).thenReturn(acc);

        ResponseEntity<Account> res = controller.get("ACC001");

        assertEquals(200, res.getStatusCodeValue());
        assertEquals("ACC001", res.getBody().getAccountNumber());
    }

    @Test
    void testDeposit() {
        Account acc = new Account("ACC001", "John");
        acc.setBalance(1500.0);

        when(service.deposit("ACC001", 500.0)).thenReturn(acc);

        AmountRequest req = new AmountRequest();
        req.setAmount(500.0);

        ResponseEntity<Account> res = controller.deposit("ACC001", req);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1500.0, res.getBody().getBalance());
    }

    @Test
    void testWithdraw() {
        Account acc = new Account("ACC001", "John");
        acc.setBalance(500.0);

        when(service.withdraw("ACC001", 300.0)).thenReturn(acc);

        AmountRequest req = new AmountRequest();
        req.setAmount(300.0);

        ResponseEntity<Account> res = controller.withdraw("ACC001", req);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(500.0, res.getBody().getBalance());//just for understanding it checks nothing
    }

    @Test
    void testTransfer() {
        TransferRequest req = new TransferRequest();
        req.setSourceAccount("A1");
        req.setDestinationAccount("A2");
        req.setAmount(100.0);

        ResponseEntity<String> res = controller.transfer(req);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals("Transfer Successful", res.getBody());
    }

    @Test
    void testGetTransactions() {
        Transaction t = new Transaction("TXN1", "DEPOSIT", 200.0, "SUCCESS", "A1", null);

        when(service.getTransactions("A1")).thenReturn(List.of(t));

        ResponseEntity<List<Transaction>> res = controller.getTxn("A1");

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1, res.getBody().size());
    }
}
