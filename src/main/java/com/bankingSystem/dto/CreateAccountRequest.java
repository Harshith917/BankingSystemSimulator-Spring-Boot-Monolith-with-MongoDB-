package com.bankingSystem.dto;

import jakarta.validation.constraints.NotBlank;

//just a clean way to receive user input
public class CreateAccountRequest {

    @NotBlank(message = "Name is required")
    private String holderName;

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
}
