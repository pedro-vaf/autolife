export const API_BASE_URL = 'http://localhost:5001/autolife';

export function getAuthToken() {
    return localStorage.getItem('token')
}

export async function fetchWithAuth(url, options = {}) {
    const token = getAuthToken()

    const config = {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` }),
            ...options.headers
        }
    }

    const response = await fetch(url, config)

    if (response.status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('login')
        window.location.reload()
    }

    return response
}