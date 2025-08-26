# Simple Inventory Management System

A simple Java-based inventory management syste## 📦 Simple Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/inventorysystem/
│   │       ├── InventoryManagementSystemApplication.java
│   │       ├── controller/
│   │       │   ├── UserController.java
│   │       │   ├── ProductController.java
│   │       │   └── TransactionController.java
│   │       ├── service/
│   │       │   ├── UserService.java
│   │       │   ├── ProductService.java
│   │       │   └── TransactionService.java
│   │       ├── repository/
│   │       │   ├── UserRepository.java
│   │       │   ├── ProductRepository.java
│   │       │   └── TransactionRepository.java
│   │       ├── model/
│   │       │   ├── User.java
│   │       │   ├── Product.java
│   │       │   └── StockTransaction.java
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       └── dto/
│   │           ├── LoginRequest.java
│   │           └── RegisterRequest.java
│   └── resources/
│       └── application.properties
└── test/ (optional)
``` built with **Spring Boot**, **JPA/Hibernate**, and **PostgreSQL**. This system uses Object-Oriented Programming principles with just **2 core modules** for easy management.

## 🎯 Project Overview

This simplified inventory system handles only the essential features:
- **User Management**: User registration, login, and authentication
- **Product Management**: Add, update, delete, and search products with stock levels
- **Transaction Management**: Record stock in/out operations

## 🏗️ Simple Architecture

### 3-Module Design
```
Module 1: USER MANAGEMENT
├── User Entity (authentication & roles)
├── UserController 
├── UserService
└── UserRepository

Module 2: PRODUCT MANAGEMENT
├── Product Entity (with built-in stock tracking)
├── ProductController 
├── ProductService
└── ProductRepository

Module 3: TRANSACTION MANAGEMENT  
├── StockTransaction Entity
├── TransactionController
├── TransactionService
└── TransactionRepository
```

### OOP Principles Applied
- **Encapsulation**: Private fields with getter/setter methods using Lombok
- **Inheritance**: Simple base entity for common fields
- **Abstraction**: Service layer for business logic

## 📋 Simple Development Plan

### Step 1: User Management Module
**Create user authentication system**

**User Entity:**
- Fields: id, username, email, password, role (ADMIN/USER), createdDate
- Simple role-based access control
- Password encryption

**Features:**
- User registration with validation
- User login with authentication
- Role-based access (ADMIN can manage all, USER can view only)
- Session management

### Step 2: Product Management Module
**Transform existing Student entity to Product**

**Product Entity:**
- Fields: id, name, description, price, sku, currentStock, minimumStock, createdBy
- Simple CRUD operations
- Built-in stock level tracking
- User tracking (who created/modified)

**Features:**
- Add/Update/Delete products (ADMIN only)
- View all products (All users)
- Search products by name or SKU
- Check low stock items

### Step 3: Transaction Management Module  
**Add simple stock tracking**

**StockTransaction Entity:**
- Fields: id, productId, transactionType (IN/OUT), quantity, date, notes, createdBy
- Simple transaction logging
- User tracking for audit trail

**Features:**
- Record stock IN (when receiving products) - ADMIN only
- Record stock OUT (when selling products) - ADMIN only
- View transaction history (All users)
- Automatic stock level updates

## � Simple Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/inventorysystem/
│   │       ├── InventoryManagementSystemApplication.java
│   │       ├── controller/
│   │       │   ├── ProductController.java
│   │       │   └── TransactionController.java
│   │       ├── service/
│   │       │   ├── ProductService.java
│   │       │   └── TransactionService.java
│   │       ├── repository/
│   │       │   ├── ProductRepository.java
│   │       │   └── TransactionRepository.java
│   │       └── model/
│   │           ├── Product.java
│   │           └── StockTransaction.java
│   └── resources/
│       └── application.properties
└── test/ (optional)
```

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL (or use H2 for development)
- NetBeans IDE

### Running the Application
```bash
# Navigate to project directory
cd StudentMS-main

# Run with Maven
mvn spring-boot:run
```

## 📊 Simple API Endpoints

### User Management
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/users/profile` - Get current user profile
- `GET /api/users` - Get all users (ADMIN only)

### Product Management
- `GET /api/products` - Get all products (All users)
- `GET /api/products/{id}` - Get product by ID (All users)
- `POST /api/products` - Create new product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `DELETE /api/products/{id}` - Delete product (ADMIN only)
- `GET /api/products/low-stock` - Get products with low stock (All users)

### Transaction Management  
- `GET /api/transactions` - Get all transactions (All users)
- `POST /api/transactions/stock-in` - Add stock to product (ADMIN only)
- `POST /api/transactions/stock-out` - Remove stock from product (ADMIN only)
- `GET /api/transactions/product/{productId}` - Get product transaction history (All users)

## 🔄 Migration Steps

### Step 1: Add User Management (New Module)
1. Create `User.java` entity with authentication fields
2. Create `UserController.java` for registration/login
3. Create `UserService.java` for user business logic
4. Create `UserRepository.java` for user data access
5. Add basic security configuration

### Step 2: Transform Student to Product
1. Rename `Student.java` → `Product.java`
2. Update fields and add stock-related properties
3. Update `StudentController` → `ProductController`  
4. Update `StudentService` → `ProductService`
5. Update `StudentRepository` → `ProductRepository`
6. Add user tracking fields (createdBy, modifiedBy)

### Step 3: Add Transaction Module
1. Create `StockTransaction.java` entity
2. Create `TransactionController.java`
3. Create `TransactionService.java` 
4. Create `TransactionRepository.java`
5. Add user tracking for transactions
6. Update database configuration

## 📝 Sample Entities

### User Entity
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String email;
    private String password; // encrypted
    private String role; // "ADMIN" or "USER"
    private LocalDateTime createdDate;
    // getters, setters, constructors
}
```

### Product Entity
```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private String name;
    private String description;
    private Double price;
    private String sku;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer createdBy; // user who created
    private LocalDateTime createdDate;
    // getters, setters, constructors
}
```

### StockTransaction Entity  
```java
@Entity
public class StockTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    private Integer productId;
    private String transactionType; // "IN" or "OUT"
    private Integer quantity;
    private LocalDateTime transactionDate;
    private String notes;
    private Integer createdBy; // user who made transaction
    // getters, setters, constructors
}
```

## 🛡️ Security Features

### Authentication & Authorization
- **Password Encryption**: BCrypt hashing for secure password storage
- **Session Management**: Simple session-based authentication
- **Role-based Access Control**: 
  - **ADMIN**: Full access (CRUD operations on products/transactions)
  - **USER**: Read-only access (view products and transactions)

### Security Implementation
- Login validation with encrypted password comparison
- Session tracking for authenticated users
- Role-based endpoint protection
- Input validation and sanitization

## 🔧 Additional Dependencies Required

Add to `pom.xml`:
```xml
<!-- Security & Password Encryption -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Session Management -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-core</artifactId>
</dependency>
```

---

*A simple, focused approach to inventory management! 🎯*
