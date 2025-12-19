// const API_URL = "http://localhost:8080";
const API_URL = "https://vehiclemaintenanceplatform.onrender.com";

document.getElementById("loginBtn").addEventListener("click", login);
document.getElementById("registerBtn").addEventListener("click", showRegister);
document.getElementById("backToLoginBtn").addEventListener("click", showLogin);
document.getElementById("submitRegisterBtn").addEventListener("click", submitRegister);

function login() {
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const errorMsg = document.getElementById("errorMsg");
    const loginBtn = document.getElementById("loginBtn");
    const loader = document.getElementById("loginLoader");

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    errorMsg.classList.add("hidden");
    errorMsg.textContent = "";

    if (!username || !password) {
        errorMsg.textContent = "Please enter username and password.";
        errorMsg.classList.remove("hidden");
        return;
    }

    // 🔄 show loading state
    loader.classList.remove("hidden");
    loginBtn.disabled = true;
    loginBtn.textContent = "Моля изчакайте...";

    fetch(`${API_URL}/users/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Invalid credentials");
        }
        return response.json();
    })
    .then(data => {
        // store logged user
        localStorage.setItem("loggedUser", JSON.stringify({
            username: data.username
        }));

        window.location.href = "mainPage.html";
    })
    .catch(error => {
        errorMsg.textContent = "Login failed. Check username or password.";
        errorMsg.classList.remove("hidden");

        // 🔁 restore UI
        loader.classList.add("hidden");
        loginBtn.disabled = false;
        loginBtn.textContent = "Влез";
    });
}

function showRegister() {
    document.getElementById("loginCard").classList.add("hidden");
    document.getElementById("registerCard").classList.remove("hidden");
}

function showLogin() {
    document.getElementById("registerCard").classList.add("hidden");
    document.getElementById("loginCard").classList.remove("hidden");
}

function submitRegister() {
    const username = document.getElementById("regUsername").value.trim();
    const password = document.getElementById("regPassword").value.trim();
    const confirm = document.getElementById("regConfirmPassword").value.trim();
    const errorMsg = document.getElementById("registerErrorMsg");

    errorMsg.classList.add("hidden");
    errorMsg.textContent = "";

    if (!username || !password || !confirm) {
        errorMsg.textContent = "All fields are required.";
        errorMsg.classList.remove("hidden");
        return;
    }

    if (password !== confirm) {
        errorMsg.textContent = "Passwords do not match.";
        errorMsg.classList.remove("hidden");
        return;
    }

    fetch(`${API_URL}/users/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
    .then(async response => {
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Registration failed");
        }
        return response.json(); // 👈 важно
    })
    .then(data => {
        alert("Successful registration!");

        localStorage.setItem("loggedUser", JSON.stringify({
            username: data.username
        }));

        window.location.href = "mainPage.html";
    })
    .catch(err => {
        errorMsg.textContent = err.message || "Registration failed.";
        errorMsg.classList.remove("hidden");
    });
}