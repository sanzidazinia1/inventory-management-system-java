# API Testing Guide for Inventory Management System

## 📋 Prerequisites

-   Java 17 or higher installed
-   PostgreSQL database running on localhost:5432
-   Database named `inventory_db` created

## 🚀 Running the Application

### Option 1: Using Maven Wrapper (Recommended)

```bash
cd /Users/ansaralisarkar/Desktop/my-project/sanzida-java/inventory-management-system-java
./mvnw spring-boot:run
```

### Option 2: Using Maven (if installed)

```bash
cd /Users/ansaralisarkar/Desktop/my-project/sanzida-java/inventory-management-system-java
mvn spring-boot:run
```

### Option 3: Using H2 Database for Testing

1. Comment out PostgreSQL settings in `application.properties`
2. Uncomment H2 settings
3. Run the application

## 🧪 API Testing Endpoints

**Base URL**: `http://localhost:8080`

### 1. User Authentication

#### Register a new admin user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

#### Register a regular user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "user1@example.com",
    "password": "user123",
    "role": "USER"
  }'
```

#### Login (get session)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -b cookies.txt -c cookies.txt \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. Product Management

#### Get all products (Public access)

```bash
curl -X GET http://localhost:8080/api/products
```

#### Create a new product (Admin only)

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "name": "Laptop",
    "description": "Dell Latitude 7420",
    "price": 899.99,
    "sku": "LAP001",
    "currentStock": 10,
    "minimumStock": 3
  }'
```

#### Update a product (Admin only)

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "name": "Gaming Laptop",
    "description": "Dell G15 Gaming Laptop",
    "price": 1299.99,
    "sku": "LAP001",
    "currentStock": 8,
    "minimumStock": 2
  }'
```

#### Search products

```bash
curl -X GET "http://localhost:8080/api/products/search?keyword=laptop"
```

#### Get low stock products

```bash
curl -X GET http://localhost:8080/api/products/low-stock
```

#### Get product analytics

```bash
curl -X GET http://localhost:8080/api/products/analytics
```

### 3. Stock Transaction Management

#### Stock In (Admin only)

```bash
curl -X POST http://localhost:8080/api/transactions/stock-in \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "productId": 1,
    "quantity": 5,
    "notes": "Received new shipment"
  }'
```

#### Stock Out (Admin only)

```bash
curl -X POST http://localhost:8080/api/transactions/stock-out \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "productId": 1,
    "quantity": 2,
    "notes": "Sold to customer"
  }'
```

#### Get all transactions (Public access)

```bash
curl -X GET http://localhost:8080/api/transactions
```

#### Get transactions by product

```bash
curl -X GET http://localhost:8080/api/transactions/product/1
```

#### Get transaction analytics

```bash
curl -X GET http://localhost:8080/api/transactions/analytics
```

## 🔍 Expected Response Formats

### Success Response

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### Error Response

```json
{
    "success": false,
    "message": "Error description"
}
```

## 📊 Sample Test Scenarios

### Complete Workflow Test:

1. **Register Admin User**
2. **Login as Admin**
3. **Create Products**
4. **Stock In Operations**
5. **View Products and Stock Levels**
6. **Stock Out Operations**
7. **Check Low Stock Items**
8. **View Transaction History**
9. **Register Regular User**
10. **Login as Regular User**
11. **Try to Create Product (Should Fail)**
12. **View Products (Should Work)**

## 🛠️ Troubleshooting

### Common Issues:

1. **Database Connection Failed**: Make sure PostgreSQL is running
2. **Session Expired**: Login again to get new session
3. **Access Denied**: Make sure you're logged in with correct role
4. **Validation Error**: Check request body format and required fields

### Check Application Status:

```bash
curl -X GET http://localhost:8080/actuator/health
```

### View H2 Console (if using H2):

Open `http://localhost:8080/h2-console` in browser

-   JDBC URL: `jdbc:h2:mem:inventorydb`
-   Username: `sa`
-   Password: (empty)

## 🎯 Success Criteria

✅ Application starts without errors  
✅ User registration works  
✅ User login creates session  
✅ Admin can create/update/delete products  
✅ Regular users can only view products  
✅ Stock transactions update product quantities  
✅ Low stock detection works  
✅ All endpoints return proper JSON responses  
✅ Role-based access control works  
✅ Database tables are created automatically
