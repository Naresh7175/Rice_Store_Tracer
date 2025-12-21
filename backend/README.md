# Rice Store Management System

## Prerequisites
1. Java 17+
2. Node.js and npm
3. PostgreSQL Database

## Setup

### Database
1. Create a PostgreSQL database named `ricestore`.
   ```sql
   CREATE DATABASE ricestore;
   ```
2. Update database credentials in `backend/src/main/resources/application.properties` if they differ from default (user: postgres, pass: postgres).

### Backend
1. Navigate to `backend` directory.
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   (Or run `RiceStoreApplication.java` in your IDE)

### Frontend
1. Navigate to `frontend` directory.
2. Install dependencies (if not already):
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm start
   ```
   (or `ng serve`)
4. Open browser at `http://localhost:4200`.

## Features
- **Inventory**: Add and manage rice bags.
- **Customers**: Manage customer details and track debts.
- **Sales**: Process sales, handle discounts, and partial payments.
- **Reports**: View revenue reports.
