// ===================== CONFIG =====================
const API_BASE = '';  // empty = same origin (localhost:8080)

function getToken() { return localStorage.getItem('token'); }
function getUser()  { return JSON.parse(localStorage.getItem('user') || 'null'); }
function setAuth(token, user) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
}
function clearAuth() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
}

// ===================== API CALL =====================
async function apiCall(method, path, body = null) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json' };
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);

    try {
        const res = await fetch(API_BASE + '/api' + path, opts);

        if (res.status === 401) {
            clearAuth();
            window.location.href = '/login.html';
            return null;
        }
        if (res.status === 403) {
            showToast('Access denied. Please log in again.', 'error');
            clearAuth();
            window.location.href = '/login.html';
            return null;
        }

        const data = await res.json();
        return data;
    } catch(e) {
        showToast('Network error. Is the server running on port 8080?', 'error');
        return null;
    }
}

// ===================== FILE DOWNLOAD =====================
async function apiDownload(path) {
    const token = getToken();
    const res = await fetch('/api' + path, {
        headers: { 'Authorization': 'Bearer ' + token }
    });
    if (!res.ok) { showToast('Download failed', 'error'); return; }
    const blob = await res.blob();
    const cd = res.headers.get('Content-Disposition') || '';
    const match = cd.match(/filename=([^;]+)/);
    const filename = match ? match[1] : 'report';
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url; a.download = filename;
    document.body.appendChild(a); a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}

// ===================== TOAST =====================
let toastContainer = null;
function showToast(msg, type = 'info') {
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        document.body.appendChild(toastContainer);
    }
    const toast = document.createElement('div');
    toast.className = 'toast ' + type;
    const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
    toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${msg}</span>`;
    toastContainer.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity 0.3s';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// ===================== AUTH GUARD =====================
function requireAuth(expectedRole) {
    const token = getToken();
    const user = getUser();
    if (!token || !user) {
        window.location.href = '/login.html';
        return false;
    }
    if (expectedRole && user.role !== expectedRole) {
        const redirect = user.role === 'ADMIN'
            ? '/pages/admin-dashboard.html'
            : '/pages/staff-dashboard.html';
        window.location.href = redirect;
        return false;
    }
    return true;
}

function logout() {
    clearAuth();
    window.location.href = '/login.html';
}

// ===================== MODAL HELPERS =====================
function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

// ===================== NOTIFICATION BADGE =====================
async function updateNotifBadge(role) {
    const prefix = role === 'ADMIN' ? '/admin' : '/staff';
    const res = await apiCall('GET', prefix + '/notifications/unread-count');
    if (res && res.success) {
        const count = res.data;
        const badge = document.getElementById('notifBadge');
        if (badge) {
            badge.textContent = count;
            badge.style.display = count > 0 ? 'block' : 'none';
        }
        const sb = document.getElementById('sidebarNotifBadge');
        if (sb) {
            sb.textContent = count;
            sb.style.display = count > 0 ? 'inline' : 'none';
        }
    }
}

// ===================== FORMATTERS =====================
function fmtDate(str) {
    if (!str) return '-';
    return new Date(str).toLocaleString('en-IN', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function fmtMoney(val) {
    if (val === null || val === undefined) return '₹0';
    return '₹' + parseFloat(val).toLocaleString('en-IN', {
        minimumFractionDigits: 2, maximumFractionDigits: 2
    });
}
