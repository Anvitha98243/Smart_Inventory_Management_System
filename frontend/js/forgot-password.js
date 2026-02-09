// Include config functions
const API_BASE_URL = 'http://localhost:8080/api/auth';
const API_ENDPOINTS = {
    FORGOT_PASSWORD: `${API_BASE_URL}/forgot-password`,
    RESET_PASSWORD: `${API_BASE_URL}/reset-password`
};

const Utils = {
    showMessage: (elementId, message, type) => {
        const messageEl = document.getElementById(elementId);
        if (messageEl) {
            messageEl.textContent = message;
            messageEl.className = `message ${type}`;
            messageEl.style.display = 'block';
            setTimeout(() => {
                messageEl.style.display = 'none';
            }, 8000);
        }
    },
    apiCall: async (url, method = 'GET', data = null) => {
        try {
            const options = {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            if (data && (method === 'POST' || method === 'PUT')) {
                options.body = JSON.stringify(data);
            }
            const response = await fetch(url, options);
            const responseData = await response.json();
            return responseData;
        } catch (error) {
            console.error('API Error:', error);
            return { success: false, message: 'Network error. Please try again.' };
        }
    }
};

let userEmail = '';

// Forgot Password Form Handler
document.getElementById('forgotPasswordForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = document.getElementById('email').value.trim();
    
    if (!email) {
        Utils.showMessage('message', 'Please enter your email address', 'error');
        return;
    }
    
    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        Utils.showMessage('message', 'Please enter a valid email address', 'error');
        return;
    }
    
    // Show loading state
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Sending...';
    submitBtn.disabled = true;
    
    try {
        const response = await Utils.apiCall(API_ENDPOINTS.FORGOT_PASSWORD, 'POST', {
            email: email
        });
        
        if (response.success) {
            userEmail = email;
            Utils.showMessage('message', response.message, 'success');
            
            // Show reset section
            document.getElementById('resetSection').style.display = 'block';
            document.getElementById('forgotPasswordForm').style.display = 'none';
        } else {
            Utils.showMessage('message', response.message || 'Failed to send reset token', 'error');
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
        }
    } catch (error) {
        Utils.showMessage('message', 'An error occurred. Please try again.', 'error');
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
});

// Reset Password Form Handler
document.getElementById('resetPasswordForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const resetToken = document.getElementById('resetToken').value.trim();
    const newPassword = document.getElementById('newPassword').value;
    const confirmNewPassword = document.getElementById('confirmNewPassword').value;
    
    // Validation
    if (!resetToken || !newPassword || !confirmNewPassword) {
        Utils.showMessage('resetMessage', 'Please fill in all fields', 'error');
        return;
    }
    
    if (newPassword.length < 6) {
        Utils.showMessage('resetMessage', 'Password must be at least 6 characters long', 'error');
        return;
    }
    
    if (newPassword !== confirmNewPassword) {
        Utils.showMessage('resetMessage', 'Passwords do not match', 'error');
        return;
    }
    
    // Show loading state
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Resetting...';
    submitBtn.disabled = true;
    
    try {
        const response = await Utils.apiCall(API_ENDPOINTS.RESET_PASSWORD, 'POST', {
            email: userEmail,
            resetToken: resetToken,
            newPassword: newPassword
        });
        
        if (response.success) {
            Utils.showMessage('resetMessage', 'Password reset successful! Redirecting to login...', 'success');
            
            // Redirect to login page
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            Utils.showMessage('resetMessage', response.message || 'Password reset failed', 'error');
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
        }
    } catch (error) {
        Utils.showMessage('resetMessage', 'An error occurred. Please try again.', 'error');
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
});
