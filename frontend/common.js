const API_BASE = localStorage.getItem('apiBase') || 'http://localhost:8080/api/v1';
const TOKEN_KEY = 'token';

function getToken() {
  return localStorage.getItem(TOKEN_KEY) || '';
}

function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

async function apiFetch(path, options = {}) {
  const headers = Object.assign({ 'Content-Type': 'application/json' }, options.headers || {});
  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, Object.assign({}, options, { headers }));
  } catch (e) {
    throw new Error(`网络请求失败，请确认后端已启动且可访问：${API_BASE}`);
  }

  if (response.status === 401) {
    alert('登录已过期，请重新登录');
    location.href = './login.html';
    throw new Error('Unauthorized');
  }

  const contentType = response.headers.get('content-type') || '';
  if (!contentType.includes('application/json')) {
    throw new Error('接口返回格式异常，请检查后端服务');
  }

  const data = await response.json();
  if (data.code !== 0) {
    throw new Error(data.message || '请求失败');
  }
  return data.data;
}

function paginate(list, page, pageSize) {
  const total = list.length;
  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const currentPage = Math.min(Math.max(page, 1), totalPages);
  const start = (currentPage - 1) * pageSize;
  const end = start + pageSize;
  return {
    pageItems: list.slice(start, end),
    total,
    totalPages,
    currentPage
  };
}
