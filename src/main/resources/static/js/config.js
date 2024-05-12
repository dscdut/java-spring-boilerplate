const getUrl = window.location
const BASE_URL = getUrl.protocol + "//" + getUrl.host
const VALID_STATUS = [200, 201, 202, 203, 204];
async function fetchMethod(route, data = {}, method = "get") {
    const ALLOWED_METHODS = ["get", "post", "put", "patch", "delete"];

    if (ALLOWED_METHODS.includes(method.toLowerCase())) {
        let requestData = {
            method: method,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }

        if (method === "get")
            delete requestData.body;

        return (await fetch((BASE_URL + route), requestData)).json();
    }
}

function numberWithCommas(x, fix = 0) {
    if (Number(x) === x && x % 1 !== 0)
        x = x.toFixed(fix);

    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function showToastError(message = "Error", title = 'Error') {
    toast({
        title,
        message,
        type: 'error'
    })
}

function showToastInfor(message = "Infor", title = "Infor") {
   toast({
       title,
       message,
       type: 'infor'
   })
}

function showToastWarning(message = "Warning", title = "Warning") {
    toast({
        title,
        message,
        type: 'warning'
    })
}

function toast({title = '', message = '', type = 'infor'}) {
    const main = document.getElementById('toast')
    if (main) {
        const toastElement = document.createElement('div')

        const autoRemoveId = setTimeout(() => {
            main.removeChild(toastElement)
        }, 3000 + 1000)

        toastElement.onclick = event => {
            if (event.target.closest('.toast__close')) {
                main.removeChild(toastElement)
                clearTimeout(autoRemoveId)

            }
        }
        const icons = {
            success: 'fas fa-check-circle',
            infor: 'fas fa-info-circle',
            warning: 'fas fa-exclamation-circle',
            error: 'fas fa-exclamation-circle',
        }

        toastElement.classList.add('toast', `toast--${type}`)
        const delay = (3000 / 1000).toFixed(2);
        toastElement.style.animation = `slideInLeft ease .5s, fadeOut linear 1s ${delay}s forwards`
        toastElement.innerHTML = `
                <div class="toast__icon">
                    <i class="${icons[type]}"></i>
                </div>
                <div class="toast__body">
                    <h3 class="toast__title">${title}</h3>
                    <p class="toast__msg">${message}</p>
                </div>
                <div class="toast__close">
                    <i class="fas fa-times"></i>
                </div>`
        main.append(toastElement)

    }
}

function getTime(date) {
    let minute = (date.getMinutes() >= 0 && date.getMinutes() < 10) ? `0${date.getMinutes()}` : date.getMinutes();
    return date.getHours() + ":" + minute;
}

function getDiffHour(startDate, endDate) {
    return (Math.abs(endDate - startDate) / 36e5).toFixed(3);
}

function getDateTime(date) {
    if (!date) return
    return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${getTime(date)}`
}
