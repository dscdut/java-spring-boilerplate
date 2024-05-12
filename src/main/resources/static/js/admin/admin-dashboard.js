let selectedPeriod;
let ALL_BILLS = []

fetchBills()
async function fetchBills() {
    ALL_BILLS = await fetchMethod("/tableOrders");
}

ListDateClick();
function ListDateClick() {
    let ButtonClick = document.querySelector(".sales .drop-down i");
    let ListDate = document.querySelector(".list-date")
    let SpanChange = ListDate.parentNode.parentNode.querySelector("span");
    ButtonClick.onclick = function () {
        ListDate.classList.add("activeFlex");
        window.onmouseup = function (event) {
            if (ListDate.contains(event.target)) {
                if (event.target.tagName === "LI") {
                    SpanChange.textContent = event.target.textContent.toUpperCase();
                    ListDate.classList.remove("activeFlex");
                }
            } else {
                ListDate.classList.remove("activeFlex");
            }
        }
    }
}

periodClick();
function periodClick(){
    let period = document.querySelectorAll("#menu-list-date li[id]");
    for (const periodElement of period) {
        periodElement.onclick = function() {
            selectedPeriod = periodElement.id;
            let {totalAmount, numberOfOrder} = loadStatistics();
            document.getElementById("number-of-order").textContent = numberOfOrder;
            document.getElementById("total-amount").textContent = totalAmount;
        }
    }
}

function getOrdersInPeriod(startDay, endDay){
    let ordersInPeriod = [];
    let from = startDay.getTime();
    let to = endDay.getTime();
    ALL_BILLS.forEach(order => {
        let orderDate = new Date(order.beginAt).getTime();
        if(orderDate >= from && orderDate <= to)
            ordersInPeriod.push(order);
    })
    return ordersInPeriod;
}

function loadStatistics(){
    let totalAmount = 0;
    let numberOfOrder;
    let ordersInPeriod;
    let today = new Date();
    today.setHours(0,0,0,0);
    let endOfDay = new Date(today);
    endOfDay.setHours(23,59,59,999);
    console.log(selectedPeriod);
    switch(selectedPeriod){
        case "today":{
            ordersInPeriod = getOrdersInPeriod(today, endOfDay);
            break;
        }
        case "yesterday":{
            let yesterday = new Date(today);
            yesterday.setDate(yesterday.getDate() - 1);
            ordersInPeriod = getOrdersInPeriod(yesterday, today);
            break;
        }
        case "this-week":{
            let monday = getMonday(today);
            ordersInPeriod = getOrdersInPeriod(monday, endOfDay);
            break;
        }
        case "last-7-days":{
            let week = new Date(today);
            week.setDate(week.getDate() - 7);
            week.setHours(0,0,0,0);
            ordersInPeriod = getOrdersInPeriod(week, endOfDay);
            break;
        }
        case "this-month":{
            let firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
            firstDayOfMonth.setHours(0,0,0,0);
            ordersInPeriod = getOrdersInPeriod(firstDayOfMonth, endOfDay);
            break;
        }
        case "last-month":{
            let firstDayInPreviousMonth = new Date(today.getFullYear(), today.getMonth() - 1, 1);
            firstDayInPreviousMonth.setHours(0,0,0,0);
            let lastDayInPreviousMonth = new Date();
            lastDayInPreviousMonth.setDate(today.setDate(1) - 1);
            lastDayInPreviousMonth.setHours(23,59,59,999);
            ordersInPeriod = getOrdersInPeriod(firstDayInPreviousMonth, lastDayInPreviousMonth);
            break;
        }
        default: {
            ordersInPeriod = getOrdersInPeriod(today, endOfDay);
        }
    }
    numberOfOrder = ordersInPeriod.length;
    ordersInPeriod.forEach(order => {
        totalAmount += order.total;
    });
   return {
       numberOfOrder: numberWithCommas(numberOfOrder),
       totalAmount: numberWithCommas(totalAmount)
   }
}
