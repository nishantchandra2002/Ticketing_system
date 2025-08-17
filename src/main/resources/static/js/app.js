const API_BASE = "http://localhost:8080/api";

// Save token
function saveToken(token) {
    localStorage.setItem("jwt", token);
}
// Get token
function getToken() {
    return localStorage.getItem("jwt");
}
// Clear token (logout)
function logout() {
    localStorage.removeItem("jwt");
    window.location.href = "/";
}
// Fetch wrapper
async function apiRequest(endpoint, method = "GET", body = null) {
    const headers = { "Content-Type": "application/json" };
    const token = getToken();
    if (token) headers["Authorization"] = "Bearer " + token;

    const response = await fetch(API_BASE + endpoint, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
    });

    if (!response.ok) {
        if (response.status === 403) {
            alert("Session expired. Please login again.");
            logout();
        }
        throw new Error(await response.text());
    }
    return response.json();
}
