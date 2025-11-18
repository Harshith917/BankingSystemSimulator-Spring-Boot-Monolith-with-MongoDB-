package com.bankingSystem.service.impl;

import com.bankingSystem.exception.*;
import com.bankingSystem.model.Account;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.repository.AccountRepository;
import com.bankingSystem.repository.TransactionRepository;
import com.bankingSystem.service.AccountService;
import com.bankingSystem.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);


    private final AccountRepository accRepo;
    private final TransactionRepository txnRepo;

    public AccountServiceImpl(AccountRepository accRepo, TransactionRepository txnRepo) {
        this.accRepo = accRepo;
        this.txnRepo = txnRepo;
    }

    @Override
    public Account createAccount(String name) {

        log.info("Service: Creating account for holder='{}'", name);

        String accNo = IdGenerator.generateAccountNumber(name);
        log.info("Service: Generated account number='{}' for holder='{}'", accNo, name);

        Account acc = new Account(accNo, name);
        Account saved = accRepo.save(acc);
        log.info("Service: Account saved in DB with accountNumber='{}'", saved.getAccountNumber());

        return saved;
    }

    private void validateAccountNumberFormat(String accNo) {
        // Account number must be: 3 letters + 4 digits  e.g. ASH1234
        if (accNo == null || !accNo.matches("^[A-Z]{3}[0-9]{4}$")) {
            throw new InvalidAccountNumberException("Invalid account number format");
        }
        log.info("Service: Account number '{}' passed validation.", accNo);

    }



    @Override
    public Account getAccount(String accNo) {

        log.info("Service: Fetching account='{}'", accNo);

        validateAccountNumberFormat(accNo);

        Account acc = accRepo.findByAccountNumber(accNo)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found"));

        log.info("Service: Account='{}' found with balance={}", accNo, acc.getBalance());

        return acc;
    }

    @Override
    public Account deposit(String accNo, Double amt) {

        log.info("Service: Deposit request: account='{}', amount={}", accNo, amt);

        validateAccountNumberFormat(accNo);
        if (amt == null || amt <= 0) throw new InvalidAmountException("Invalid Amount");

        Account acc = getAccount(accNo);

        double newBalance = acc.getBalance() + amt;
        log.info("Service: New balance after deposit = {}", newBalance);

        acc.setBalance(newBalance);

        Transaction txn = new Transaction(
                IdGenerator.generateTransactionId(),
                "DEPOSIT",
                amt,
                "SUCCESS",
                accNo,
                null
        );

        txnRepo.save(txn);
        log.info("Service: Deposit transaction created txnId='{}'", txn.getTransactionId());

        acc.getTransactionIds().add(txn.getTransactionId());

        Account updated = accRepo.save(acc);
        log.info("Service: Account updated after deposit account='{}'", accNo);

        return updated;
    }

    @Override
    public Account withdraw(String accNo, Double amt) {

        log.info("Service: Withdraw request: account='{}', amount={}", accNo, amt);

        validateAccountNumberFormat(accNo);
        if (amt == null || amt <= 0) throw new InvalidAmountException("Invalid Amount");

        Account acc = getAccount(accNo);

        if (acc.getBalance() < amt) {
            log.error("Service: Insufficient balance for account='{}'", accNo);
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        double newBalance = acc.getBalance() - amt;
        log.info("Service: New balance after withdrawal = {}", newBalance);

        acc.setBalance(newBalance);

        Transaction txn = new Transaction(
                IdGenerator.generateTransactionId(),
                "WITHDRAW",
                amt,
                "SUCCESS",
                accNo,
                null
        );

        txnRepo.save(txn);
        log.info("Service: Withdrawal transaction created txnId='{}'", txn.getTransactionId());

        acc.getTransactionIds().add(txn.getTransactionId());

        Account updated = accRepo.save(acc);
        log.info("Service: Account updated after withdrawal account='{}'", accNo);

        return updated;
    }


    @Override
    public void transfer(String src, String dest, Double amt) {

        log.info("Service: Transfer request: from='{}' to='{}' amount={}", src, dest, amt);

        validateAccountNumberFormat(src);
        validateAccountNumberFormat(dest);

        if (amt == null || amt <= 0) throw new InvalidAmountException("Invalid Amount");
        if (src.equals(dest)) throw new InvalidAmountException("Source and Destination cannot be same");

        Account source = getAccount(src);

        if (source.getBalance() < amt) {
            log.error("Service: Insufficient balance for transfer from='{}'", src);
            throw new InsufficientBalanceException("Insufficient balance");
        }

        log.info("Service: Withdrawing {} from '{}'", amt, src);
        source.setBalance(source.getBalance() - amt);
        Transaction withdrawTxn = new Transaction(
                IdGenerator.generateTransactionId(),
                "WITHDRAW",
                amt,
                "SUCCESS",
                src,
                null
        );
        txnRepo.save(withdrawTxn);
        source.getTransactionIds().add(withdrawTxn.getTransactionId());
        accRepo.save(source);

        log.info("Service: Depositing {} to '{}'", amt, dest);
        Account destination = getAccount(dest);
        destination.setBalance(destination.getBalance() + amt);
        Transaction depositTxn = new Transaction(
                IdGenerator.generateTransactionId(),
                "DEPOSIT",
                amt,
                "SUCCESS",
                dest,
                null
        );
        txnRepo.save(depositTxn);
        destination.getTransactionIds().add(depositTxn.getTransactionId());
        accRepo.save(destination);

        log.info("Service: Creating transfer transaction record");
        Transaction transferTxn = new Transaction(
                IdGenerator.generateTransactionId(),
                "TRANSFER",
                amt,
                "SUCCESS",
                src,
                dest
        );
        txnRepo.save(transferTxn);

        source.getTransactionIds().add(transferTxn.getTransactionId());
        destination.getTransactionIds().add(transferTxn.getTransactionId());

        accRepo.save(source);
        accRepo.save(destination);

        log.info("Service: Transfer completed successfully from='{}' to='{}' amount={}", src, dest, amt);
    }


    @Override
    public Account updateHolderName(String accNo, String newHolderName) {
        Account acc = getAccount(accNo);
        acc.setHolderName(newHolderName);
        return accRepo.save(acc);
    }

    @Override
    public void deleteAccount(String accNo) {

        validateAccountNumberFormat(accNo);

        Account acc = getAccount(accNo);
        // simple soft delete or hard delete; here we'll hard delete
        accRepo.delete(acc);
    }


    @Override
    public List<Transaction> getTransactions(String accNo) {

        validateAccountNumberFormat(accNo);

        return txnRepo.findBySourceAccountOrDestinationAccount(accNo, accNo);
    }

}
