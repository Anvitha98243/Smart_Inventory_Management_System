# 📧 EMAIL SETUP GUIDE
## Smart Inventory - Password Reset Email Feature

---

## 🎯 What's New?

Your app can now send **real emails** for:
- ✅ Password reset with secure token
- ✅ Welcome emails for new users

---

## 📋 SETUP STEPS

### **STEP 1: Get Gmail App Password** ⏱️ 3 minutes

Gmail requires an "App Password" (not your regular password) for security.

1. **Go to your Google Account:**
   - Visit: https://myaccount.google.com/
   - Sign in with your Gmail

2. **Enable 2-Step Verification** (if not already enabled):
   - Click "Security" in left menu
   - Scroll to "2-Step Verification"
   - Click "Get Started" and follow steps

3. **Generate App Password:**
   - Go back to "Security"
   - Scroll down to "App passwords"
   - Click on it (you may need to sign in again)
   - Select:
     - App: "Mail"
     - Device: "Windows Computer"
   - Click "Generate"
   - **COPY THE 16-CHARACTER PASSWORD** (e.g., `abcd efgh ijkl mnop`)

---

### **STEP 2: Update Your Files** ⏱️ 2 minutes

Replace these 3 files in your project:

#### **File 1: pom.xml**
Location: `C:\Users\anvit\OneDrive\Desktop\smart-inventory-auth\backend\pom.xml`

- **Replace entire file** with the updated `pom.xml` I provided
- This adds email support dependency

#### **File 2: application.properties**
Location: `C:\Users\anvit\OneDrive\Desktop\smart-inventory-auth\backend\src\main\resources\application.properties`

- **Replace entire file** with the updated `application.properties`
- **THEN EDIT these 2 lines:**
  ```properties
  spring.mail.username=YOUR_EMAIL@gmail.com
  spring.mail.password=YOUR_APP_PASSWORD
  ```
  
  Replace with:
  ```properties
  spring.mail.username=youractual@gmail.com
  spring.mail.password=abcd efgh ijkl mnop
  ```
  (Use your real Gmail and the 16-char app password you copied)

- **Also update this line:**
  ```properties
  app.email.from=youractual@gmail.com
  ```

#### **File 3: EmailService.java**
Location: `C:\Users\anvit\OneDrive\Desktop\smart-inventory-auth\backend\src\main\java\com\inventory\service\EmailService.java`

- **Create new file** (this file doesn't exist yet)
- Copy the entire `EmailService.java` I provided

#### **File 4: AuthService.java**
Location: `C:\Users\anvit\OneDrive\Desktop\smart-inventory-auth\backend\src\main\java\com\inventory\service\AuthService.java`

- **Replace entire file** with the updated `AuthService.java`

---

### **STEP 3: Rebuild and Run** ⏱️ 2 minutes

1. **Stop the backend** (if running)
   - Press `Ctrl + C` in the Command Prompt where it's running

2. **Clean and rebuild:**
   ```cmd
   cd C:\Users\anvit\OneDrive\Desktop\smart-inventory-auth\backend
   mvn clean install
   ```

3. **Run the application:**
   ```cmd
   cd target
   java -jar smart-inventory-auth-1.0.0.jar
   ```

4. **Look for this in the logs:**
   ```
   Started InventoryApplication in X.XXX seconds
   ```

---

## 🎯 TEST THE EMAIL FEATURE

### **Test 1: Password Reset**

1. **Open frontend:**
   - Double-click `frontend/login.html`

2. **Click "Forgot Password"**

3. **Enter email:** `admin@inventory.com`

4. **Click Submit**

5. **Check your Gmail inbox:**
   - You should receive an email with:
     - Subject: "Password Reset Request - Smart Inventory System"
     - A reset token inside

6. **Copy the token from email**

7. **Go to reset password page:**
   - Enter email: `admin@inventory.com`
   - Paste the token
   - Enter new password
   - Click Submit

8. **Try logging in** with new password!

---

### **Test 2: Welcome Email**

1. **Click "Sign Up"** on login page

2. **Create new account:**
   - Username: `testuser`
   - Email: `your-email@gmail.com` (use YOUR email)
   - Password: `Test123!`
   - Full Name: `Test User`

3. **Click Register**

4. **Check your email:**
   - You should receive a welcome email!

---

## 🔧 TROUBLESHOOTING

### **"Failed to send email"**

**Check these:**

1. **Gmail credentials correct?**
   - Email: Must be full Gmail address
   - Password: Must be the 16-char App Password (not regular password)

2. **2-Step Verification enabled?**
   - App passwords only work with 2-Step Verification

3. **Less Secure Apps?**
   - With App Password, this is NOT needed

4. **Check backend logs:**
   ```
   ❌ Failed to send email: [error message]
   ```
   The error message will tell you what's wrong

---

### **"Authentication failed"**

This means Gmail credentials are wrong.

**Fix:**
- Double-check email and app password in `application.properties`
- Make sure there are no extra spaces
- App password should be 16 characters (spaces are optional)

---

### **"Connection timeout"**

Your internet or firewall is blocking SMTP.

**Fix:**
- Check internet connection
- Try disabling antivirus temporarily
- Check if port 587 is blocked

---

## 📧 EMAIL TEMPLATES

### **Password Reset Email Includes:**
- Professional HTML design
- User's full name
- Reset token in a box
- 1-hour expiry warning
- Instructions
- Security notice

### **Welcome Email Includes:**
- Welcome message
- Username reminder
- Next steps
- Professional branding

---

## 🔐 SECURITY FEATURES

✅ **App Password** - More secure than regular password
✅ **Token Expiry** - Tokens expire in 1 hour
✅ **One-time Use** - Token deleted after password reset
✅ **HTML Emails** - Professional and readable
✅ **Error Handling** - Graceful fallback if email fails

---

## ⚙️ CONFIGURATION OPTIONS

You can customize in `application.properties`:

```properties
# Change email provider (Gmail, Outlook, etc.)
spring.mail.host=smtp.gmail.com

# Change port (587 for TLS, 465 for SSL)
spring.mail.port=587

# Change token expiry (currently 1 hour)
# Edit in AuthService.java line 114:
# .plusHours(1) → .plusHours(24)

# Change frontend URL
app.frontend.url=http://yoursite.com
```

---

## 📝 SUMMARY

**What you need:**
1. Gmail account with 2-Step Verification
2. Gmail App Password
3. Updated 4 files
4. Rebuild with `mvn clean install`

**What you get:**
- ✅ Automatic password reset emails
- ✅ Welcome emails for new users
- ✅ Professional HTML email templates
- ✅ Secure token-based reset

---

## 🎉 That's It!

Email feature is now ready to use!

For questions or issues, check the troubleshooting section above.

---

**Created on:** February 8, 2026
**Version:** 1.0.0 with Email Support
