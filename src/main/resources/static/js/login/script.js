async function postMethod(route, data) {
    return (await fetch((BASE_URL + route), {
        method: 'post',
        body: new URLSearchParams(data)
    })).json();
}

const formLogin = document.querySelector("form#login")

formLogin.addEventListener("submit", async event => {
    const formData = new FormData(formLogin)
    let data = {
        'email': formData.get("email"),
        'password': formData.get("password")
    }
    event.preventDefault();
    let res = await postMethod("/login", data);
    if (VALID_STATUS.includes(res.status)) {
        return window.location.replace(BASE_URL + res.message);
    }

    let errorMessageDiv = document.getElementById("login-error");
    errorMessageDiv.innerHTML = `
        <div class="error-message" >
        <p>${res.message}</p>
        </div>
        `
})