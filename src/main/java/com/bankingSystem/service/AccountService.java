package com.bankingSystem.service;

import com.bankingSystem.model.Account;
import com.bankingSystem.model.Transaction;

import java.util.List;

public interface AccountService {

    Account createAccount(String name);

    Account getAccount(String accNo);

    Account deposit(String accNo, Double amt);

    Account withdraw(String accNo, Double amt);

    Account updateHolderName(String accNo, String newHolderName);

    void deleteAccount(String accNo);

    void transfer(String src, String dest, Double amt);

    List<Transaction> getTransactions(String accNo);

}
