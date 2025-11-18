package com.bankingSystem.controller;

import com.bankingSystem.dto.AmountRequest;
import com.bankingSystem.dto.CreateAccountRequest;
import com.bankingSystem.dto.TransferRequest;
import com.bankingSystem.model.Account;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService service) {
        this.service = service;
    }

    //acc creation
    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountRequest req) {
        Account created = service.createAccount(req.getHolderName());
        log.info("Created account: accountNumber='{}', holder='{}'", created.getAccountNumber(), req.getHolderName());
        return ResponseEntity.status(201).body(created);  // Explicit
    }

    //getting acc
    @GetMapping("/{accNo}")
    public ResponseEntity<Account> get(@PathVariable String accNo) {
        Account account = service.getAccount(accNo);
        log.info("Fetched account: accountNumber='{}', balance={}", accNo, account.getBalance());
        return ResponseEntity.status(200).body(account); // Explicit
    }

    // UPDATE HOLDER NAME - 200 OK
    @PutMapping("/{accNo}")
    public ResponseEntity<Account> updateHolderName(
            @PathVariable String accNo,
            @Valid @RequestBody CreateAccountRequest req) {

        Account updated = service.updateHolderName(accNo, req.getHolderName());
        log.info("Updated account holder: accountNumber='{}', newHolder='{}'", accNo, req.getHolderName());
        return ResponseEntity.status(200).body(updated); // Explicit
    }

    // DELETE ACCOUNT - 204 No Content
    @DeleteMapping("/{accNo}")
    public ResponseEntity<Void> delete(@PathVariable String accNo) {
        service.deleteAccount(accNo);
        log.info("Deleted account: accountNumber='{}'", accNo);
        return ResponseEntity.status(204).build(); // Explicit
    }

    // DEPOSIT - 200 OK
    @PutMapping("/{accNo}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accNo,
            @Valid @RequestBody AmountRequest req) {

        Account updated = service.deposit(accNo, req.getAmount());
        log.info("Deposit: accountNumber='{}', amount={}, newBalance={}", accNo, req.getAmount(), updated.getBalance());
        return ResponseEntity.status(200).body(updated); // Explicit
    }

    // WITHDRAW - 200 OK
    @PutMapping("/{accNo}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accNo,
            @Valid @RequestBody AmountRequest req) {

        Account updated = service.withdraw(accNo, req.getAmount());
        log.info("Withdraw: accountNumber='{}', amount={}, newBalance={}", accNo, req.getAmount(), updated.getBalance());
        return ResponseEntity.status(200).body(updated); // Explicit
    }

    // TRANSFER - 200 OK
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest req) {
        log.info("Initiating transfer: from='{}' to='{}' amount={}", req.getSourceAccount(), req.getDestinationAccount(), req.getAmount());
        service.transfer(req.getSourceAccount(), req.getDestinationAccount(), req.getAmount());
        log.info("Transfer successful: from='{}' to='{}' amount={}", req.getSourceAccount(), req.getDestinationAccount(), req.getAmount());
        return ResponseEntity.status(200).body("Transfer Successful"); // Explicit
    }

    // TRANSACTION HISTORY - 200 OK
    @GetMapping("/{accNo}/transactions")
    public ResponseEntity<List<Transaction>> getTxn(@PathVariable String accNo) {
        List<Transaction> txns = service.getTransactions(accNo);
        log.info("Fetched {} transactions for account '{}'", txns.size(), accNo);
        return ResponseEntity.status(200).body(txns); // Explicit
    }
}









//package com.bankingSystem.controller;
//
//import com.bankingSystem.dto.AmountRequest;
//import com.bankingSystem.dto.CreateAccountRequest;
//import com.bankingSystem.dto.TransferRequest;
//import com.bankingSystem.model.Account;
//import com.bankingSystem.model.Transaction;
//import com.bankingSystem.service.AccountService;
//import jakarta.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//        import java.util.List;
//
//@RestController
//@RequestMapping("/api/accounts")
//public class AccountController {
//
//    private final AccountService service;
//    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
//
//    public AccountController(AccountService service) {
//        this.service = service;
//    }
//
//    // CREATE ACCOUNT - 201 Created
//    @PostMapping
//    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountRequest req) {
//        Account created = service.createAccount(req.getHolderName());
//        log.info("Created new account: {}", created.getAccountNumber());
//        return ResponseEntity.status(201).body(created);
//    }
//
//    // GET ACCOUNT - 200 OK
//    @GetMapping("/{accNo}")
//    public ResponseEntity<Account> get(@PathVariable String accNo) {
//        log.info("Fetching account {}", accNo);
//        return ResponseEntity.ok(service.getAccount(accNo));
//    }
//
//    // UPDATE HOLDER NAME - 200 OK
//    @PutMapping("/{accNo}")
//    public ResponseEntity<Account> updateHolderName(
//            @PathVariable String accNo,
//            @Valid @RequestBody CreateAccountRequest req) {
//
//        log.info("Updating account {} holder name to {}", accNo, req.getHolderName());
//        Account updated = service.updateHolderName(accNo, req.getHolderName());
//        return ResponseEntity.ok(updated);
//    }
//
//    // DELETE ACCOUNT - 204 No Content
//    @DeleteMapping("/{accNo}")
//    public ResponseEntity<Void> delete(@PathVariable String accNo) {
//        log.info("Deleting account {}", accNo);
//        service.deleteAccount(accNo);
//        return ResponseEntity.noContent().build();
//    }
//
//    // DEPOSIT - 200 OK
//    @PutMapping("/{accNo}/deposit")
//    public ResponseEntity<Account> deposit(
//            @PathVariable String accNo,
//            @Valid @RequestBody AmountRequest req) {
//
//        log.info("Deposit {} into account {}", req.getAmount(), accNo);
//        return ResponseEntity.ok(service.deposit(accNo, req.getAmount()));
//    }
//
//    // WITHDRAW - 200 OK
//    @PutMapping("/{accNo}/withdraw")
//    public ResponseEntity<Account> withdraw(
//            @PathVariable String accNo,
//            @Valid @RequestBody AmountRequest req) {
//
//        log.info("Withdraw {} from account {}", req.getAmount(), accNo);
//        return ResponseEntity.ok(service.withdraw(accNo, req.getAmount()));
//    }
//
//    // TRANSFER - 200 OK
//    @PostMapping("/transfer")
//    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest req) {
//        log.info("Transfer {} from {} to {}", req.getAmount(), req.getSourceAccount(), req.getDestinationAccount());
//        service.transfer(req.getSourceAccount(), req.getDestinationAccount(), req.getAmount());
//        return ResponseEntity.ok("Transfer Successful");
//    }
//
//    // TRANSACTION HISTORY - 200 OK
//    @GetMapping("/{accNo}/transactions")
//    public ResponseEntity<List<Transaction>> getTxn(@PathVariable String accNo) {
//        log.info("Fetching transactions for {}", accNo);
//        return ResponseEntity.ok(service.getTransactions(accNo));
//    }
//}
