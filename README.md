# Smart Inventory Management System
## Module 1: Authentication & Authorization

### Complete Setup Guide

---

## 📋 Table of Contents
1. Prerequisites
2. Database Setup
3. Backend Setup (Spring Boot)
4. Frontend Setup
5. Running the Application
6. Testing the System
7. API Documentation
8. Troubleshooting

---

## 1. Prerequisites

### Required Software:
- **Java JDK 11 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **MySQL 8.0 or higher** - [Download](https://dev.mysql.com/downloads/)
- **Maven 3.6 or higher** - [Download](https://maven.apache.org/download.cgi)
- **Any Web Browser** (Chrome, Firefox, Edge)
- **Text Editor/IDE** (VS Code, IntelliJ IDEA, Eclipse)

### Verify Installation:
```bash
java -version
mysql --version
mvn -version
```

---

## 2. Database Setup

### Step 1: Start MySQL Server
- **Windows**: Start MySQL from Services or MySQL Workbench
- **Mac/Linux**: `sudo systemctl start mysql` or `sudo service mysql start`

### Step 2: Create Database and Tables

**Option A: Using MySQL Workbench**
1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Open the file `database-schema.sql`
4. Execute the script

**Option B: Using Command Line**
```bash
# Login to MySQL
mysql -u root -p

# Run the schema file
source /path/to/database-schema.sql

# Or copy-paste the SQL commands from database-schema.sql
```

### Step 3: Verify Database Creation
```sql
USE smart_inventory;
SHOW TABLES;
SELECT * FROM users;
```

You should see 2 default users:
- **Admin**: username: `admin`, password: `admin123`
- **Employee**: username: `john_doe`, password: `employee123`

---

## 3. Backend Setup (Spring Boot)

### Step 1: Navigate to Backend Directory
```bash
cd smart-inventory-auth/backend
```

### Step 2: Update Database Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Build the Project
```bash
mvn clean install
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```

Or using Java:
```bash
mvn package
java -jar target/smart-inventory-auth-1.0.0.jar
```

### Expected Output:
```
Smart Inventory Management System - Authentication Module Started!
API running on: http://localhost:8080
```

---

## 4. Frontend Setup

### Step 1: Navigate to Frontend Directory
```bash
cd smart-inventory-auth/frontend
```

### Step 2: Open in Web Browser

**Option A: Using Live Server (VS Code Extension)**
1. Install "Live Server" extension in VS Code
2. Right-click on `login.html`
3. Select "Open with Live Server"

**Option B: Direct File Opening**
1. Simply double-click `login.html`
2. Browser will open the page

**Option C: Using Python HTTP Server**
```bash
# Python 3
python -m http.server 5500

# Python 2
python -m SimpleHTTPServer 5500
```
Then open: http://localhost:5500/login.html

---

## 5. Running the Application

### Complete Startup Checklist:

1. ✅ MySQL Server is running
2. ✅ Database `smart_inventory` exists with tables
3. ✅ Backend Spring Boot application is running on port 8080
4. ✅ Frontend is opened in web browser

### Access the Application:
- **Login Page**: Open `login.html` in browser
- **Signup Page**: Click "Sign Up" link or open `signup.html`
- **Forgot Password**: Click "Forgot Password" or open `forgot-password.html`

---

## 6. Testing the System

### Test 1: Login with Default Users

**Admin User:**
- Username: `admin`
- Password: `admin123`
- Expected: Login successful, redirect to dashboard, see admin panel with user management

**Employee User:**
- Username: `john_doe`
- Password: `employee123`
- Expected: Login successful, redirect to dashboard, no admin panel visible

### Test 2: User Registration
1. Go to signup page
2. Fill in all fields:
   - Full Name: Test User
   - Username: testuser
   - Email: test@example.com
   - Password: test123
   - Role: EMPLOYEE
3. Click "Sign Up"
4. Expected: Account created, auto-login, redirect to dashboard

### Test 3: Password Reset
1. Go to forgot password page
2. Enter registered email
3. Click "Send Reset Token"
4. Copy the reset token from the success message
5. Enter token and new password
6. Click "Reset Password"
7. Expected: Password reset successful, redirect to login
8. Login with new password

### Test 4: Admin User Management
1. Login as admin
2. Scroll to "User Management" section
3. View all users in table
4. Click "Delete" on a user (not yourself)
5. Expected: User deleted successfully, table updated

### Test 5: Database Verification
```sql
-- Check if new user was created
SELECT * FROM users WHERE username = 'testuser';

-- Check password reset
SELECT reset_token, reset_token_expiry FROM users WHERE email = 'test@example.com';

-- Check last login update
SELECT username, last_login FROM users WHERE username = 'admin';
```

---

## 7. API Documentation

### Base URL: `http://localhost:8080/api/auth`

### Endpoints:

#### 1. User Registration
```http
POST /signup
Content-Type: application/json

{
    "username": "newuser",
    "email": "user@example.com",
    "password": "password123",
    "fullName": "New User",
    "role": "EMPLOYEE"
}

Response:
{
    "success": true,
    "message": "User registered successfully!",
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
        "id": 3,
        "username": "newuser",
        "email": "user@example.com",
        "fullName": "New User",
        "role": "EMPLOYEE",
        "isActive": true,
        "createdAt": "2026-02-08T10:30:00"
    }
}
```

#### 2. User Login
```http
POST /login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}

Response:
{
    "success": true,
    "message": "Login successful!",
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "user": { ... }
}
```

#### 3. Forgot Password
```http
POST /forgot-password
Content-Type: application/json

{
    "email": "admin@inventory.com"
}

Response:
{
    "success": true,
    "message": "Reset token generated. Token: abc123-def456-ghi789"
}
```

#### 4. Reset Password
```http
POST /reset-password
Content-Type: application/json

{
    "email": "admin@inventory.com",
    "resetToken": "abc123-def456-ghi789",
    "newPassword": "newpassword123"
}

Response:
{
    "success": true,
    "message": "Password reset successful!"
}
```

#### 5. Get All Users (Admin Only)
```http
GET /users
Authorization: Bearer {token}

Response:
[
    {
        "id": 1,
        "username": "admin",
        "email": "admin@inventory.com",
        "fullName": "System Administrator",
        "role": "ADMIN",
        "isActive": true,
        "createdAt": "2026-02-08T10:00:00"
    },
    ...
]
```

#### 6. Delete User (Admin Only)
```http
DELETE /users/{id}
Authorization: Bearer {token}

Response:
{
    "success": true,
    "message": "User deleted successfully!"
}
```

---

## 8. Troubleshooting

### Issue: Backend won't start

**Error: "Port 8080 already in use"**
```bash
# Find process using port 8080
# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux:
lsof -ti:8080 | xargs kill -9
```

**Error: "Cannot connect to database"**
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database `smart_inventory` exists

### Issue: Frontend can't connect to backend

**Error: "Network error" or CORS error**
- Verify backend is running on http://localhost:8080
- Check browser console for specific error
- Ensure CORS is properly configured in backend

### Issue: Login not working

**Check:**
1. Backend logs for errors
2. Browser Network tab (F12) for API response
3. Database for user existence:
   ```sql
   SELECT * FROM users WHERE username = 'admin';
   ```

### Issue: Password reset token not working

**Common causes:**
- Token expired (valid for 1 hour)
- Wrong email or token
- Check database:
   ```sql
   SELECT reset_token, reset_token_expiry FROM users WHERE email = 'your@email.com';
   ```

---

## 9. Project Structure

```
smart-inventory-auth/
├── database-schema.sql
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/inventory/
│       ├── InventoryApplication.java
│       ├── model/
│       │   ├── User.java
│       │   ├── LoginRequest.java
│       │   ├── SignupRequest.java
│       │   ├── AuthResponse.java
│       │   ├── UserDTO.java
│       │   └── ResetPasswordRequest.java
│       ├── repository/
│       │   └── UserRepository.java
│       ├── service/
│       │   └── AuthService.java
│       ├── controller/
│       │   └── AuthController.java
│       ├── config/
│       │   └── CorsConfig.java
│       └── util/
│           └── JwtUtil.java
└── frontend/
    ├── login.html
    ├── signup.html
    ├── forgot-password.html
    ├── dashboard.html
    ├── css/
    │   └── style.css
    └── js/
        ├── config.js
        ├── login.js
        ├── signup.js
        ├── forgot-password.js
        └── dashboard.js
```

---

## 10. Features Implemented ✅

### Authentication Features:
- ✅ Secure user registration with validation
- ✅ User login with JWT token generation
- ✅ Password hashing using BCrypt
- ✅ Session management with JWT
- ✅ Remember me functionality
- ✅ Auto-redirect for authenticated users

### Password Management:
- ✅ Forgot password functionality
- ✅ Reset token generation (1-hour validity)
- ✅ Secure password reset process
- ✅ Password confirmation validation

### Authorization Features:
- ✅ Role-based access control (ADMIN, EMPLOYEE)
- ✅ Admin user management panel
- ✅ Create/Delete employee accounts (Admin only)
- ✅ Protected routes and endpoints
- ✅ User status management (Active/Inactive)

### Security Features:
- ✅ BCrypt password encryption
- ✅ JWT-based authentication
- ✅ Input validation on frontend and backend
- ✅ SQL injection prevention (JPA/Hibernate)
- ✅ XSS protection
- ✅ CORS configuration

### User Experience:
- ✅ Responsive design for all devices
- ✅ Loading states and user feedback
- ✅ Error handling and validation messages
- ✅ Professional UI with modern styling
- ✅ Demo credentials for testing

---

## 11. Next Steps (Future Modules)

- Module 2: Product/Inventory Management
- Module 3: Stock Tracking
- Module 4: Reports & Analytics
- Module 5: Supplier Management

---

## 12. Support

For issues or questions:
1. Check the troubleshooting section
2. Review browser console logs (F12)
3. Check backend logs in terminal
4. Verify database connectivity

---

**Module 1 - Authentication & Authorization - COMPLETE! ✅**
