# Banking System Simulator (Spring Boot + MongoDB)

A clean, modular, and fully tested **Banking Services Application** built using **Spring Boot**, **MongoDB**, **REST APIs**, **Exception Handling**, and **JUnit + Mockito**.

This project simulates real-world banking operations such as:

* Creating bank accounts
* Depositing and withdrawing money
* Transferring funds
* Tracking transaction history
* Managing account details

All functionalities follow a layered **Controller → Service → Repository → Model** architecture.

---

##  Features

###  Account Management

* Auto-generated account numbers (`ABC1234` format)
* Create, update, retrieve, delete accounts
* MongoDB-backed persistence
* Input validation (name, amount, account number format)

###  Transactions

* Deposit
* Withdraw
* Transfer (with multiple transaction logs)
* Stored in separate MongoDB `transactions` collection
* Linked to accounts via accountNumber & transactionIds

###  Clean Exception Handling

Custom exceptions:

* `InvalidAmountException`
* `InsufficientBalanceException`
* `AccountNotFoundException`
* `InvalidAccountNumberException`

Handled globally via `@ControllerAdvice`.

###  Logging

Every API logs operations using SLF4J.

###  Testing (JUnit + Mockito)

* Controller tests
* Service tests
* Exception handler tests
* Model tests
* Utility tests
* Repository placeholder tests

* Unit Test Coverage Report

Below is the coverage generated using JaCoCo:

(images/coverage.png)

=> Over **81% coverage** achievable.

---

##  Project Structure

```
com.bankingSystem
├── controller
│   └── AccountController.java
├── service
│   └── AccountService.java
│   └── impl/AccountServiceImpl.java
├── repository
│   └── AccountRepository.java
│   └── TransactionRepository.java
├── model
│   └── Account.java
│   └── Transaction.java
├── dto
│   ├── CreateAccountRequest.java
│   ├── AmountRequest.java
│   └── TransferRequest.java
├── exception
│   ├── AccountNotFoundException.java
│   ├── InvalidAmountException.java
│   ├── InsufficientBalanceException.java
│   ├── InvalidAccountNumberException.java
│   └── GlobalExceptionHandler.java
├── util
│   └── IdGenerator.java
└── BankingSystemApplication.java
```

---

##  MongoDB Setup

Add this to `application.properties`:

```
spring.data.mongodb.uri=mongodb://localhost:27017/bankingdb
```

MongoDB Collections:

* `accounts`
* `transactions`

---

##  REST API Endpoints

### ▶ Create Account

**POST** `/api/accounts`

```json
{
  "holderName": "John Doe"
}
```

**Response:** `201 Created`

---

### ▶ Get Account

**GET** `/api/accounts/{accountNumber}`

---

### ▶ Update Holder Name

**PUT** `/api/accounts/{accountNumber}`

```json
{
  "holderName": "New Name"
}
```

---

### ▶ Delete Account

**DELETE** `/api/accounts/{accountNumber}` → `204 No Content`

---

### ▶ Deposit

**PUT** `/api/accounts/{accNo}/deposit`

```json
{ "amount": 500 }
```

---

### ▶ Withdraw

**PUT** `/api/accounts/{accNo}/withdraw`

```json
{ "amount": 200 }
```

---

### ▶ Transfer

**POST** `/api/accounts/transfer`

```json
{
  "sourceAccount": "JOH1234",
  "destinationAccount": "ANN5678",
  "amount": 100
}
```

---

### ▶ Get Transactions

**GET** `/api/accounts/{accNo}/transactions`

---

##  Testing

Frameworks used: **JUnit 5 + Mockito**

Tests included:

* AccountControllerTest
* GlobalExceptionHandlerTest
* AccountServiceImplTest
* ModelTest
* IdGeneratorTest
* Application Startup Test

These ensure correctness of:

* Business logic
* API behavior
* Error handling
* Utility functions

---

##  Technologies Used

* Spring Boot
* Spring Data MongoDB
* JUnit 5
* Mockito
* SLF4J Logging
* Jakarta Bean Validation
* Maven

---

##  How to Run

### 1. Start MongoDB

```
mongod
```

### 2. Run the Spring Boot application

```
mvn spring-boot:run
```

### 3. Test APIs using Postman.

---

##  Conclusion

This Banking System Simulator is a complete, production-like backend application following best practices:

* Layered architecture
* REST API standards
* Exception handling
* MongoDB persistence
* Unit testing
* Logging



---



