package com.bankingSystem.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testAccountNotFoundHandling() {
        AccountNotFoundException ex = new AccountNotFoundException("Not Found");
        ResponseEntity<String> response = handler.notFound(ex);//spring calls this auto but......

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Not Found", response.getBody());
    }

    @Test
    void testInvalidAmountHandling() {
        InvalidAmountException ex = new InvalidAmountException("Invalid");
        ResponseEntity<String> response = handler.badRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid", response.getBody());
    }

    @Test
    void testInsufficientBalanceHandling() {
        InsufficientBalanceException ex = new InsufficientBalanceException("Low");
        ResponseEntity<String> response = handler.badRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Low", response.getBody());
    }

    @Test
    void testInvalidAccountNumberHandling() {
        InvalidAccountNumberException ex = new InvalidAccountNumberException("Invalid Format");
        ResponseEntity<String> response = handler.invalidAccountNumber(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid Format", response.getBody());
    }


}
