package com.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Send password reset email with token
     */
    public void sendPasswordResetEmail(String toEmail, String resetToken, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - Smart Inventory System");

            String htmlContent = buildPasswordResetEmailHtml(resetToken, fullName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            
            System.out.println("✅ Password reset email sent successfully to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    /**
     * Send welcome email to new users
     */
    public void sendWelcomeEmail(String toEmail, String fullName, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Smart Inventory System!");

            String htmlContent = buildWelcomeEmailHtml(fullName, username);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            
            System.out.println("✅ Welcome email sent successfully to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("⚠️ Failed to send welcome email: " + e.getMessage());
            // Don't throw exception for welcome email - registration should still succeed
        }
    }

    /**
     * Build HTML content for password reset email
     */
    private String buildPasswordResetEmailHtml(String resetToken, String fullName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .token-box { background: white; border: 2px dashed #667eea; padding: 20px; margin: 20px 0; text-align: center; border-radius: 8px; }" +
                "        .token { font-size: 24px; font-weight: bold; color: #667eea; letter-spacing: 2px; font-family: monospace; }" +
                "        .button { display: inline-block; background: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                "        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }" +
                "        .warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>🔐 Password Reset Request</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Hello <strong>" + fullName + "</strong>,</p>" +
                "            <p>We received a request to reset your password for your Smart Inventory System account.</p>" +
                "            " +
                "            <div class='token-box'>" +
                "                <p style='margin: 0 0 10px 0; color: #666;'>Your Reset Token:</p>" +
                "                <div class='token'>" + resetToken + "</div>" +
                "            </div>" +
                "            " +
                "            <div class='warning'>" +
                "                <strong>⏰ Important:</strong> This token will expire in <strong>1 hour</strong>" +
                "            </div>" +
                "            " +
                "            <p><strong>How to reset your password:</strong></p>" +
                "            <ol>" +
                "                <li>Go to the password reset page</li>" +
                "                <li>Enter your email address</li>" +
                "                <li>Copy and paste the token above</li>" +
                "                <li>Enter your new password</li>" +
                "            </ol>" +
                "            " +
                "            <div style='background: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin: 20px 0;'>" +
                "                <strong>ℹ️ Didn't request this?</strong><br>" +
                "                If you didn't request a password reset, please ignore this email. Your password will remain unchanged." +
                "            </div>" +
                "            " +
                "            <div class='footer'>" +
                "                <p>This is an automated email from Smart Inventory Management System</p>" +
                "                <p>&copy; 2026 Smart Inventory System. All rights reserved.</p>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build HTML content for welcome email
     */
    private String buildWelcomeEmailHtml(String fullName, String username) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .welcome-box { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                "        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>🎉 Welcome to Smart Inventory!</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Hello <strong>" + fullName + "</strong>,</p>" +
                "            <p>Welcome to the Smart Inventory Management System! Your account has been created successfully.</p>" +
                "            " +
                "            <div class='welcome-box'>" +
                "                <h3>Your Account Details:</h3>" +
                "                <p><strong>Username:</strong> " + username + "</p>" +
                "                <p><strong>Email:</strong> You're receiving this email at your registered address</p>" +
                "            </div>" +
                "            " +
                "            <p><strong>Next Steps:</strong></p>" +
                "            <ul>" +
                "                <li>Log in to your dashboard</li>" +
                "                <li>Complete your profile</li>" +
                "                <li>Start managing your inventory</li>" +
                "            </ul>" +
                "            " +
                "            <p>If you have any questions, feel free to reach out to our support team.</p>" +
                "            " +
                "            <div class='footer'>" +
                "                <p>This is an automated email from Smart Inventory Management System</p>" +
                "                <p>&copy; 2026 Smart Inventory System. All rights reserved.</p>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
