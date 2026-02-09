// API Configuration
const API_BASE_URL = 'http://localhost:8080/api/auth';

// API Endpoints
const API_ENDPOINTS = {
    LOGIN: `${API_BASE_URL}/login`,
    SIGNUP: `${API_BASE_URL}/signup`,
    FORGOT_PASSWORD: `${API_BASE_URL}/forgot-password`,
    RESET_PASSWORD: `${API_BASE_URL}/reset-password`,
    USERS: `${API_BASE_URL}/users`,
    DELETE_USER: (id) => `${API_BASE_URL}/users/${id}`,
    DEACTIVATE_USER: (id) => `${API_BASE_URL}/users/${id}/deactivate`
};

// Local Storage Keys
const STORAGE_KEYS = {
    TOKEN: 'authToken',
    USER: 'currentUser'
};

// Utility Functions
const Utils = {
    // Show message
    showMessage: (elementId, message, type) => {
        const messageEl = document.getElementById(elementId);
        if (messageEl) {
            messageEl.textContent = message;
            messageEl.className = `message ${type}`;
            messageEl.style.display = 'block';
            
            // Auto hide after 5 seconds
            setTimeout(() => {
                messageEl.style.display = 'none';
            }, 5000);
        }
    },
    
    // Save to local storage
    saveToStorage: (key, value) => {
        localStorage.setItem(key, JSON.stringify(value));
    },
    
    // Get from local storage
    getFromStorage: (key) => {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    },
    
    // Remove from local storage
    removeFromStorage: (key) => {
        localStorage.removeItem(key);
    },
    
    // Check if user is authenticated
    isAuthenticated: () => {
        return !!Utils.getFromStorage(STORAGE_KEYS.TOKEN);
    },
    
    // Get current user
    getCurrentUser: () => {
        return Utils.getFromStorage(STORAGE_KEYS.USER);
    },
    
    // Logout
    logout: () => {
        Utils.removeFromStorage(STORAGE_KEYS.TOKEN);
        Utils.removeFromStorage(STORAGE_KEYS.USER);
        window.location.href = 'login.html';
    },
    
    // Format date
    formatDate: (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    },
    
    // Make API call
    apiCall: async (url, method = 'GET', data = null) => {
        try {
            const options = {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            
            // Add authorization header if token exists
            const token = Utils.getFromStorage(STORAGE_KEYS.TOKEN);
            if (token) {
                options.headers['Authorization'] = `Bearer ${token}`;
            }
            
            // Add body for POST, PUT requests
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
