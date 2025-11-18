package com.bankingSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

    @NotBlank(message = "Source account is required")
    private String sourceAccount;

    @NotBlank(message = "Destination account is required")
    private String destinationAccount;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private Double amount;


    public String getSourceAccount() {
        return sourceAccount;
    }
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
