// Include config functions
const API_BASE_URL = 'http://localhost:8080/api/auth';
const API_ENDPOINTS = {
    SIGNUP: `${API_BASE_URL}/signup`
};
const STORAGE_KEYS = {
    TOKEN: 'authToken',
    USER: 'currentUser'
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
            }, 5000);
        }
    },
    saveToStorage: (key, value) => {
        localStorage.setItem(key, JSON.stringify(value));
    },
    getFromStorage: (key) => {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    },
    isAuthenticated: () => {
        return !!Utils.getFromStorage(STORAGE_KEYS.TOKEN);
    },
    apiCall: async (url, method = 'GET', data = null) => {
        try {
            const options = {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            const token = Utils.getFromStorage(STORAGE_KEYS.TOKEN);
            if (token) {
                options.headers['Authorization'] = `Bearer ${token}`;
            }
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

// Check if already logged in
if (Utils.isAuthenticated()) {
    window.location.href = 'dashboard.html';
}

// Signup Form Handler
document.getElementById('signupForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const fullName = document.getElementById('fullName').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const role = document.getElementById('role').value;
    
    // Validation
    if (!fullName || !username || !email || !password || !confirmPassword) {
        Utils.showMessage('message', 'Please fill in all fields', 'error');
        return;
    }
    
    if (password.length < 6) {
        Utils.showMessage('message', 'Password must be at least 6 characters long', 'error');
        return;
    }
    
    if (password !== confirmPassword) {
        Utils.showMessage('message', 'Passwords do not match', 'error');
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
    submitBtn.textContent = 'Creating Account...';
    submitBtn.disabled = true;
    
    try {
        const response = await Utils.apiCall(API_ENDPOINTS.SIGNUP, 'POST', {
            fullName: fullName,
            username: username,
            email: email,
            password: password,
            role: role
        });
        
        if (response.success) {
            // Save token and user data
            Utils.saveToStorage(STORAGE_KEYS.TOKEN, response.token);
            Utils.saveToStorage(STORAGE_KEYS.USER, response.user);
            
            Utils.showMessage('message', 'Account created successfully! Redirecting...', 'success');
            
            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1500);
        } else {
            Utils.showMessage('message', response.message || 'Signup failed', 'error');
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
        }
    } catch (error) {
        Utils.showMessage('message', 'An error occurred. Please try again.', 'error');
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
});
