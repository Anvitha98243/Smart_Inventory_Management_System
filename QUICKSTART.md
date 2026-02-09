# QUICK START GUIDE
## Smart Inventory Management - Authentication Module

### 🚀 5-Minute Setup

---

## Step 1: Database Setup (2 minutes)

```bash
# Login to MySQL
mysql -u root -p

# Copy and paste these commands:
```

```sql
CREATE DATABASE IF NOT EXISTS smart_inventory;
USE smart_inventory;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    reset_token VARCHAR(255) NULL,
    reset_token_expiry TIMESTAMP NULL
);

INSERT INTO users (username, email, password, full_name, role) 
VALUES ('admin', 'admin@inventory.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'ADMIN');

INSERT INTO users (username, email, password, full_name, role) 
VALUES ('john_doe', 'john@inventory.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John Doe', 'EMPLOYEE');
```

---

## Step 2: Backend Setup (2 minutes)

```bash
# Navigate to backend folder
cd smart-inventory-auth/backend

# Update application.properties with your MySQL credentials
# Edit: src/main/resources/application.properties
# Change: spring.datasource.username and spring.datasource.password

# Build and run
mvn clean install
mvn spring-boot:run
```

**Wait for:** "Smart Inventory Management System - Authentication Module Started!"

---

## Step 3: Frontend Setup (1 minute)

```bash
# Navigate to frontend folder
cd smart-inventory-auth/frontend

# Open login.html in your browser
# Double-click login.html OR right-click > Open with > Chrome/Firefox
```

---

## 🎯 Test It!

### Login as Admin:
- Username: `admin`
- Password: `admin123`
- ✅ Should see dashboard with user management

### Login as Employee:
- Username: `john_doe`
- Password: `employee123`
- ✅ Should see dashboard without admin panel

### Create New Account:
- Click "Sign Up"
- Fill in the form
- ✅ Should auto-login and redirect

---

## 🔧 Common Issues

**Backend won't start?**
- Check MySQL is running
- Verify database credentials in application.properties

**Can't login?**
- Check browser console (F12)
- Verify backend is running on http://localhost:8080
- Check database has users

**Frontend errors?**
- Ensure backend is running first
- Check API URL in JS files (should be localhost:8080)

---

## 📱 What You Get

✅ **Login System** - Secure authentication with JWT
✅ **User Registration** - Create new accounts
✅ **Password Reset** - Forgot password functionality
✅ **Admin Panel** - Manage users (ADMIN role only)
✅ **Role-Based Access** - ADMIN vs EMPLOYEE permissions
✅ **Responsive Design** - Works on all devices
✅ **Data Persistence** - All data stored in MySQL

---

## 🎓 Demo Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Employee | john_doe | employee123 |

---

**That's it! Module 1 is ready to use! 🎉**

For detailed documentation, see README.md
