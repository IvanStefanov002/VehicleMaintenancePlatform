/* function used to lock pages and init authorization */
function requireAuth() {
    const user = localStorage.getItem("loggedUser");
    if (!user) {
        window.location.replace("/index.html");
    }
}