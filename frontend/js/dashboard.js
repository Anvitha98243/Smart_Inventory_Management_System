// Include config functions
const API_BASE_URL = 'http://localhost:8080/api/auth';
const API_ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,
    DELETE_USER: (id) => `${API_BASE_URL}/users/${id}`
};
const STORAGE_KEYS = {
    TOKEN: 'authToken',
    USER: 'currentUser'
};

const Utils = {
    getFromStorage: (key) => {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    },
    removeFromStorage: (key) => {
        localStorage.removeItem(key);
    },
    isAuthenticated: () => {
        return !!Utils.getFromStorage(STORAGE_KEYS.TOKEN);
    },
    getCurrentUser: () => {
        return Utils.getFromStorage(STORAGE_KEYS.USER);
    },
    logout: () => {
        Utils.removeFromStorage(STORAGE_KEYS.TOKEN);
        Utils.removeFromStorage(STORAGE_KEYS.USER);
        window.location.href = 'login.html';
    },
    formatDate: (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
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

// Check authentication
if (!Utils.isAuthenticated()) {
    window.location.href = 'login.html';
}

const currentUser = Utils.getCurrentUser();

// Display welcome message
document.getElementById('welcomeUser').textContent = `Welcome, ${currentUser.fullName}!`;

// Display user profile
const userProfileHTML = `
    <p><strong>Username:</strong> <span>${currentUser.username}</span></p>
    <p><strong>Email:</strong> <span>${currentUser.email}</span></p>
    <p><strong>Full Name:</strong> <span>${currentUser.fullName}</span></p>
    <p><strong>Role:</strong> <span class="role-badge role-${currentUser.role.toLowerCase()}">${currentUser.role}</span></p>
    <p><strong>Account Status:</strong> <span class="status-badge ${currentUser.isActive ? 'status-active' : 'status-inactive'}">${currentUser.isActive ? 'Active' : 'Inactive'}</span></p>
    <p><strong>Member Since:</strong> <span>${Utils.formatDate(currentUser.createdAt)}</span></p>
    <p><strong>Last Login:</strong> <span>${Utils.formatDate(currentUser.lastLogin)}</span></p>
`;
document.getElementById('userProfile').innerHTML = userProfileHTML;

// Logout handler
document.getElementById('logoutBtn').addEventListener('click', () => {
    if (confirm('Are you sure you want to logout?')) {
        Utils.logout();
    }
});

// Admin functionality
if (currentUser.role === 'ADMIN') {
    document.getElementById('adminSection').style.display = 'block';
    loadUsers();
}

// Load all users (Admin only)
async function loadUsers() {
    try {
        const users = await Utils.apiCall(API_ENDPOINTS.USERS);
        
        if (Array.isArray(users)) {
            displayUsers(users);
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

// Display users in table
function displayUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    tbody.innerHTML = '';
    
    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.fullName}</td>
            <td><span class="role-badge role-${user.role.toLowerCase()}">${user.role}</span></td>
            <td><span class="status-badge ${user.isActive ? 'status-active' : 'status-inactive'}">${user.isActive ? 'Active' : 'Inactive'}</span></td>
            <td>${new Date(user.createdAt).toLocaleDateString()}</td>
            <td>
                ${user.id !== currentUser.id ? `<button class="btn btn-danger" onclick="deleteUser(${user.id}, '${user.username}')">Delete</button>` : '<span>-</span>'}
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Delete user
async function deleteUser(userId, username) {
    if (confirm(`Are you sure you want to delete user "${username}"?`)) {
        try {
            const response = await Utils.apiCall(API_ENDPOINTS.DELETE_USER(userId), 'DELETE');
            
            if (response.success) {
                alert('User deleted successfully!');
                loadUsers(); // Reload users list
            } else {
                alert(response.message || 'Failed to delete user');
            }
        } catch (error) {
            alert('Error deleting user');
            console.error('Delete error:', error);
        }
    }
}

// Make deleteUser available globally
window.deleteUser = deleteUser;
