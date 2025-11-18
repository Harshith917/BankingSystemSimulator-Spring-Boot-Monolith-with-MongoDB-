package com.bankingSystem.service;

import com.bankingSystem.exception.*;
import com.bankingSystem.model.Account;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.repository.AccountRepository;
import com.bankingSystem.repository.TransactionRepository;
import com.bankingSystem.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accRepo;

    @Mock
    private TransactionRepository txnRepo;

    @InjectMocks
    private AccountServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    // CREATE ACCOUNT

    @Test
    void testCreateAccount() {
        String holderName = "John";
        Account saved = new Account("JOH1234", holderName);

        when(accRepo.save(any(Account.class))).thenReturn(saved);

        Account result = service.createAccount(holderName);

        assertNotNull(result);
        assertEquals("John", result.getHolderName());
        verify(accRepo, times(1)).save(any(Account.class));
    }


    // UPDATE HOLDER NAME

    @Test
    void testUpdateHolderName() {
        Account acc = new Account("ACC0001", "OldName");

        when(accRepo.findByAccountNumber("ACC0001"))
                .thenReturn(Optional.of(acc));

        when(accRepo.save(acc)).thenReturn(acc);

        Account updated = service.updateHolderName("ACC0001", "NewName");

        assertEquals("NewName", updated.getHolderName());
        verify(accRepo, times(1)).save(acc);
    }

    // -------------------------------------------------------------
    // DELETE ACCOUNT
    // -------------------------------------------------------------
    @Test
    void testDeleteAccount() {
        Account acc = new Account("ACC0001", "John");

        when(accRepo.findByAccountNumber("ACC0001"))
                .thenReturn(Optional.of(acc));

        doNothing().when(accRepo).delete(acc);

        service.deleteAccount("ACC0001");

        verify(accRepo, times(1)).delete(acc);
    }

    // -------------------------------------------------------------
    // GET TRANSACTIONS
    // -------------------------------------------------------------
    @Test
    void testGetTransactions() {
        Transaction t1 = new Transaction("T1", "DEPOSIT", 100.0, "SUCCESS", "ACC0001", null);
        Transaction t2 = new Transaction("T2", "WITHDRAW", 50.0, "SUCCESS", "ACC0001", null);

        when(txnRepo.findBySourceAccountOrDestinationAccount("ACC0001", "ACC0001"))
                .thenReturn(java.util.List.of(t1, t2));

        var list = service.getTransactions("ACC0001");

        assertEquals(2, list.size());
        assertEquals("T1", list.get(0).getTransactionId());
    }

    // -------------------------------------------------------------
    // GET ACCOUNT SUCCESS
    // -------------------------------------------------------------
    @Test
    void testGetAccount() {
        Account acc = new Account("ACC0001", "John");

        when(accRepo.findByAccountNumber("ACC0001"))
                .thenReturn(Optional.of(acc));

        Account result = service.getAccount("ACC0001");

        assertEquals("John", result.getHolderName());
    }

    @Test
    void testGetAccount_InvalidAccountNumber() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.getAccount("123"));
    }



    // -------------------------------------------------------------
    // GET ACCOUNT NOT FOUND
    // -------------------------------------------------------------
    @Test
    void testGetAccount_NotFound() {
        when(accRepo.findByAccountNumber("XYZ0001")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.getAccount("XYZ0001"));
    }

    // -------------------------------------------------------------
    // DEPOSIT SUCCESS
    // -------------------------------------------------------------
    @Test
    void testDeposit() {
        Account acc = new Account("TES1234", "TestUser");
        acc.setBalance(1000.0);

        when(accRepo.findByAccountNumber("TES1234"))
                .thenReturn(Optional.of(acc));

        when(accRepo.save(acc)).thenReturn(acc);

        Account updated = service.deposit("TES1234", 500.0);

        assertEquals(1500.0, updated.getBalance());
        verify(txnRepo, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeposit_InvalidAccountNumber() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.deposit("XYZ12", 100.0));
    }

    // -------------------------------------------------------------
    // DEPOSIT INVALID AMOUNT
    // -------------------------------------------------------------
    @Test
    void testDeposit_InvalidAmount() {
        assertThrows(InvalidAmountException.class,
                () -> service.deposit("TES1234", -50.0));
    }

    // -------------------------------------------------------------
    // WITHDRAW INSUFFICIENT FUNDS
    // -------------------------------------------------------------
    @Test
    void testWithdraw_Insufficient() {
        Account acc = new Account("ACC0001", "John");
        acc.setBalance(200.0);

        when(accRepo.findByAccountNumber("ACC0001"))
                .thenReturn(Optional.of(acc));

        assertThrows(InsufficientBalanceException.class,
                () -> service.withdraw("ACC0001", 500.0));
    }

    @Test
    void testWithdraw_InvalidAccountNumber() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.withdraw("AB12CD", 200.0));
    }

    // -------------------------------------------------------------
    // WITHDRAW INVALID AMOUNT
    // -------------------------------------------------------------
    @Test
    void testWithdraw_InvalidAmount() {
        assertThrows(InvalidAmountException.class,
                () -> service.withdraw("ACC0001", -100.0));
    }

    // -------------------------------------------------------------
    // TRANSFER SUCCESS
    // -------------------------------------------------------------
    @Test
    void testTransfer() {
        Account src = new Account("SRC1234", "John");
        src.setBalance(1000.0);

        Account dest = new Account("DST5678", "Ram");
        dest.setBalance(500.0);

        when(accRepo.findByAccountNumber("SRC1234")).thenReturn(Optional.of(src));
        when(accRepo.findByAccountNumber("DST5678")).thenReturn(Optional.of(dest));

        when(accRepo.save(any(Account.class))).thenReturn(src).thenReturn(dest);

        service.transfer("SRC1234", "DST5678", 200.0);

        verify(txnRepo, times(3)).save(any(Transaction.class));  // withdraw + deposit + transfer
        verify(accRepo, times(4)).save(any(Account.class));      // two saves each account
    }



    @Test
    void testTransfer_InvalidSourceAccount() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.transfer("BAD", "JOH1234", 100.0));
    }

    @Test
    void testTransfer_InvalidDestinationAccount() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.transfer("JOH1234", "AA12", 100.0));
    }

    @Test
    void testTransfer_SameSourceAndDestination() {
        assertThrows(InvalidAmountException.class,
                () -> service.transfer("JOH1234", "JOH1234", 200.0));
    }

    @Test
    void testGetTransactions_InvalidAccountNumber() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.getTransactions("AX1"));
    }
}
