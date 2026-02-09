# 🎉 SMART INVENTORY - WITH EMAIL FEATURE!

## ✨ What's New in This Version?

This is the **UPDATED** version of Smart Inventory Management System with **EMAIL SUPPORT**!

### 🆕 New Features:
- ✅ **Password Reset Emails** - Users receive reset tokens via email
- ✅ **Welcome Emails** - New users get a welcome email on signup
- ✅ **Professional HTML Email Templates**
- ✅ **Gmail SMTP Integration**

---

## 📋 QUICK SETUP

### **BEFORE YOU START:**

You need a **Gmail App Password** to send emails.

**Get it here:** [EMAIL-SETUP-GUIDE.md](EMAIL-SETUP-GUIDE.md) ← **Read this first!**

---

## 🚀 Installation Steps

### **Step 1: Database Setup** (2 minutes)

```bash
mysql -u root -p
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

exit;
```

---

### **Step 2: Configure Email** (3 minutes) ⚠️ **IMPORTANT!**

1. **Get Gmail App Password:**
   - Visit: https://myaccount.google.com/security
   - Enable 2-Step Verification
   - Generate App Password for "Mail"
   - Copy the 16-character password

2. **Edit `backend/src/main/resources/application.properties`:**

   Find these lines:
   ```properties
   spring.mail.username=YOUR_EMAIL@gmail.com
   spring.mail.password=YOUR_APP_PASSWORD
   app.email.from=YOUR_EMAIL@gmail.com
   ```

   Replace with your actual Gmail and app password:
   ```properties
   spring.mail.username=yourname@gmail.com
   spring.mail.password=abcd efgh ijkl mnop
   app.email.from=yourname@gmail.com
   ```

---

### **Step 3: Build & Run Backend** (3 minutes)

```bash
cd backend
mvn clean install
cd target
java -jar smart-inventory-auth-1.0.0.jar
```

Wait for: `Started InventoryApplication`

---

### **Step 4: Open Frontend** (1 minute)

1. Navigate to `frontend` folder
2. Double-click `login.html`

---

## 🎯 Test the Email Feature!

### **Test Password Reset:**

1. Click **"Forgot Password"**
2. Enter email: `admin@inventory.com`
3. **Check your Gmail inbox!** 📧
4. You'll receive an email with a reset token
5. Use the token to reset password

### **Test Welcome Email:**

1. Click **"Sign Up"**
2. Create a new account with YOUR email
3. **Check your Gmail inbox!** 📧
4. You'll receive a welcome email

---

## 📧 Email Templates Included

### Password Reset Email:
- Professional HTML design
- Reset token in a highlighted box
- 1-hour expiry warning
- Step-by-step instructions
- Security notice

### Welcome Email:
- Personalized greeting
- Account details
- Next steps guide
- Professional branding

---

## 🔧 Files Changed/Added

### **New Files:**
- `backend/src/main/java/com/inventory/service/EmailService.java` ← **NEW!**
- `EMAIL-SETUP-GUIDE.md` ← **NEW!**
- `README-EMAIL.md` ← **NEW!** (this file)

### **Updated Files:**
- `backend/pom.xml` - Added email dependency
- `backend/src/main/resources/application.properties` - Email config
- `backend/src/main/java/com/inventory/service/AuthService.java` - Email integration

### **Unchanged:**
- All frontend files (HTML, CSS, JS)
- All other backend files
- Database schema

---

## 📚 Documentation

- **[EMAIL-SETUP-GUIDE.md](EMAIL-SETUP-GUIDE.md)** - Detailed email setup instructions
- **[QUICKSTART.md](QUICKSTART.md)** - Original quick start guide
- **[README.md](README.md)** - Original project README
- **[API-TESTING.md](API-TESTING.md)** - API testing guide

---

## 🔐 Demo Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Employee | john_doe | employee123 |

---

## ⚙️ Configuration

### Change Email Provider (Optional)

Edit `application.properties`:

```properties
# For Gmail (default)
spring.mail.host=smtp.gmail.com
spring.mail.port=587

# For Outlook
spring.mail.host=smtp.office365.com
spring.mail.port=587

# For Yahoo
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
```

### Change Token Expiry

Edit `AuthService.java` line 114:
```java
.plusHours(1)  // Change to .plusHours(24) for 24 hours
```

---

## 🐛 Troubleshooting

### "Failed to send email"
- Check Gmail credentials in `application.properties`
- Verify 2-Step Verification is enabled
- Confirm App Password is correct (16 characters)

### "Authentication failed"
- You're using regular password instead of App Password
- Generate a new App Password from Google

### "Connection timeout"
- Check internet connection
- Port 587 might be blocked by firewall
- Try disabling antivirus temporarily

### Email works but doesn't arrive
- Check spam/junk folder
- Verify email address is correct
- Check Gmail's sent folder

**For more help:** See [EMAIL-SETUP-GUIDE.md](EMAIL-SETUP-GUIDE.md)

---

## ✅ What You Get

✅ **Login System** - Secure JWT authentication  
✅ **User Registration** - Create new accounts  
✅ **Password Reset** - **EMAIL-BASED** with tokens ← **NEW!**  
✅ **Welcome Emails** - Sent on signup ← **NEW!**  
✅ **Admin Panel** - User management  
✅ **Role-Based Access** - ADMIN vs EMPLOYEE  
✅ **Responsive Design** - Mobile-friendly  
✅ **Professional Emails** - HTML templates ← **NEW!**  

---

## 🎓 Tech Stack

- **Backend:** Java 11, Spring Boot 2.7.14
- **Email:** Spring Mail + Gmail SMTP ← **NEW!**
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL 8.0
- **Security:** JWT, BCrypt

---

## 📞 Support

**Having issues?**
1. Read [EMAIL-SETUP-GUIDE.md](EMAIL-SETUP-GUIDE.md) for email setup
2. Check backend logs for errors
3. Verify Gmail App Password is correct

---

## 🎉 Ready to Go!

**Your Smart Inventory System is now equipped with:**
- ✅ Email password reset
- ✅ Welcome emails
- ✅ Professional templates
- ✅ Secure token system

**Just configure your Gmail credentials and you're ready!**

---

**Version:** 1.0.0 with Email Support  
**Date:** February 8, 2026  
**License:** Educational Use
