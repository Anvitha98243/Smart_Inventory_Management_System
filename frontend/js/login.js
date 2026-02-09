// Include config.js functions
const API_BASE_URL = 'http://localhost:8080/api/auth';
const API_ENDPOINTS = {
    LOGIN: `${API_BASE_URL}/login`
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

// Login Form Handler
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    
    if (!username || !password) {
        Utils.showMessage('message', 'Please fill in all fields', 'error');
        return;
    }
    
    // Show loading state
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Logging in...';
    submitBtn.disabled = true;
    
    try {
        const response = await Utils.apiCall(API_ENDPOINTS.LOGIN, 'POST', {
            username: username,
            password: password
        });
        
        if (response.success) {
            // Save token and user data
            Utils.saveToStorage(STORAGE_KEYS.TOKEN, response.token);
            Utils.saveToStorage(STORAGE_KEYS.USER, response.user);
            
            Utils.showMessage('message', 'Login successful! Redirecting...', 'success');
            
            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1000);
        } else {
            Utils.showMessage('message', response.message || 'Login failed', 'error');
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
        }
    } catch (error) {
        Utils.showMessage('message', 'An error occurred. Please try again.', 'error');
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
});
