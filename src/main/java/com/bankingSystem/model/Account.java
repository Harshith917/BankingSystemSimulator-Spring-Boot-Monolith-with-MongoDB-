package com.bankingSystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "accounts")
public class Account {

    @Id
    private String id;
    private String accountNumber;
    private String holderName;
    private Double balance;
    private String status;
    private Instant createdAt;
    private List<String> transactionIds = new ArrayList<>();

    public Account() {}

    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
        this.status = "ACTIVE";
        this.createdAt = Instant.now();
    }


    public String getId() {
        return id;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getHolderName() {
        return holderName;
    }
    public void setHolderName(String holderName) {

        this.holderName = holderName;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getStatus() {
        return status;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public List<String> getTransactionIds() {
        return transactionIds;
    }



}
