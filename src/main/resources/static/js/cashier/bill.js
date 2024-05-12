let allBills;

async function fetchTableOrders() {
    allBills = await fetchMethod("/tableOrders/active");
}

function showBill(event) {

    let parentDiv = event.target.parentElement;
    let name = parentDiv.querySelector('p').innerText;
    let tableName = document.getElementById('bill_table-name');
    let billBoard = document.getElementById('bill-board');
    tableName.innerText = name;

    let tableId = parseInt(parentDiv.getAttribute("table-id"));

    let paymentDiv = document.getElementById('get-payment-btn');
    let openBillDiv = document.getElementById('open-bill-btn');

    let cartList = document.getElementById("product-cart-list");
    let billDropList = document.getElementById('bill-drop-list');
    cartList.innerHTML = '';
    billDropList.innerHTML = '';

    billBoard.setAttribute("table-id", tableId);
    const foundTableBills = allBills.filter(bill => bill.idTable === tableId);
    if (!foundTableBills.length) {
        setEmptyBill()
    } else {
        paymentDiv.style.display = 'flex';
        openBillDiv.style.display = 'none';

        showBillItems(foundTableBills, 0)
    }
}

function AddRowHour(cartList, beginAt, endAt, id, tablePrice) {
    let dateIn = new Date(beginAt);

    let timeIn = getTime(dateIn);

    let dateOut = new Date();
    let isPauseStatus = " Dừng tính";
    let isPaused = "false";

    if (endAt) {
        dateOut = new Date(endAt);
        isPauseStatus = " Tiếp tục";
        isPaused = "true";
    }

    let diffHour = getDiffHour(dateIn, dateOut);

    let RowList = document.createElement("div");
    RowList.setAttribute("class", "row-list");
    RowList.setAttribute("id", "time-item");
    let Order = ++cartList.querySelectorAll(".row-list").length;

    RowList.innerHTML = `
    <div class="cell-action" style="color: #6f6d6d">
                        <div class="btn-trash">
                            <i class="far fa-trash-alt"></i>
                        </div>
                    </div>
                    <div class="cell-order">
                    ${Order}
                    </div>
                    <div class="cell-name">
                    Tiền giờ
                    </div>
                    <div class="cell-time" id="cell-time" is-paused=${isPaused} onclick="openCellTimePopover(event)">
                        <button class="cell-time-icon" type="button">
                            ${timeIn}
                            <i class="far fa-clock"></i>
                        </button>
                        <div class="cell-time-popover" id="cell-time-popover">
                                
                            <div class="cell-time-popover-wrap">
                                <div class="form-field">
                                    <label class="label">Từ:</label>
                                    <div class="time-in">
                                        <span>${getDateTime(dateIn)}</span>
                                        
                                    </div>
                                </div>
                                <div class="form-field">
                                    <label class="label" >Đến:</label>
                                    <div class="time-out">
                                        <span>${getDateTime(dateOut)}</span>
                                    </div>
                                </div>
                                <div class="form-field">
                                    <label class="label" >Tổng:</label>
                                    <div class="time-counter">
                                        <span>${getHourMinute(diffHour)}</span>
                                    </div>
                                </div>
                                <button class="time-stop" onclick="stopTime()">
                                    <i class="fa fa-pause-circle">  ${isPauseStatus}</i>

                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="cell-quantity-time item-amount">
                        ${diffHour}
                    </div>
                    <div class="cell-change-price">
                        <button class="btn-change-price">
                            ${numberWithCommas(tablePrice)}
                        </button>
                    </div>
                    <div class="cell-price item-total">
                        ${numberWithCommas(diffHour * tablePrice)}
                    </div>
                </div>
    `
    let Caculator = document.getElementById("Quantity-Change-wrap");
    let timePrice = RowList.querySelector(".btn-change-price");

    timePrice.addEventListener("click", () => {
        Caculator.style.display = "flex";
        Caculator.setAttribute("status", "time-price");
        document.calc.txt.value = (timePrice.innerText).replaceAll(",", "");
    })

    cartList.appendChild(RowList);
    ScrollMain2();
}

function showBillProduct({product, amount, price, id}) {
    let RowList = document.createElement("div");
    RowList.setAttribute("class", "row-list");
    RowList.setAttribute("order-detail-id", id);
    const Order = ++document.getElementById("product-cart-list").querySelectorAll(".row-list").length;
    RowList.innerHTML = `
    <div class="cell-action" onclick="openModal(this)">
        <div class="btn-trash">
            <i class="far fa-trash-alt"></i>
        </div>
    </div>
    <div class="cell-order">
       ${Order}
    </div>
    <div class="cell-name">
        ${product.name}
    </div>
    <div class="cell-quantity" >
        <button class="btn-icon down" >
            <i class="fas fa-minus-circle"></i>
        </button>
        <p class="quantity-change item-amount" id ="quantity">${amount}</p>
        <button class="btn-icon up"  >
            <i class="fas fa-plus-circle"></i>
        </button>
    </div>
    <div class="cell-change-price">
        <button class="btn-change-price">
        ${numberWithCommas(price)}
        </button>
    </div>
    <div class="cell-price item-total">
        ${numberWithCommas(price * amount)}
    </div>
</div>
    `

    let cellQuantity = RowList.querySelector(".cell-quantity").childNodes;
    let Minus = cellQuantity[1];
    let Plus = cellQuantity[5];
    let Quantity = cellQuantity[3];

    let ChangePrice = RowList.querySelector(".btn-change-price");

    // calculator
    let Caculator = document.getElementById("Quantity-Change-wrap");
    let ButtonSkip = Caculator.querySelector(".btn-skip")
    let ButtonDone = Caculator.querySelector(".btn-done")

    Minus.addEventListener("click", function (event) {
        if (Quantity.innerText === "1")
            return;
        let tableId = parseInt(document.getElementById("bill-board").getAttribute("table-id"))
        let tableOrderId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"))
        let orderDetailId = parseInt(event.target.closest(".row-list").getAttribute("order-detail-id"));

        let foundBill = allBills.find(bill => bill.id === tableOrderId);
        if (foundBill) {
            // set update status
            foundBill.isNeedUpdate = true;

            // Decrease product amount
            let foundOrderDetail = foundBill.orderDetails.find(orderDetail => orderDetail.id === orderDetailId);
            if (foundOrderDetail) {
                --foundOrderDetail.amount;
                let tableBills = allBills.filter(bill => bill.idTable === tableId )
                showBillItems(tableBills, null, tableOrderId);
            }
        }
    })
    Plus.addEventListener("click", function (event) {
        let tableId = parseInt(document.getElementById("bill-board").getAttribute("table-id"))
        let tableOrderId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10)
        let orderDetailId = parseInt(event.target.closest(".row-list").getAttribute("order-detail-id"), 10);

        let foundBill = allBills.find(bill => bill.id === tableOrderId);
        if (foundBill) {
            // set update status
            foundBill.isNeedUpdate = true;

            let foundOrderDetail = foundBill.orderDetails.find(orderDetail => orderDetail.id === orderDetailId);
            if (foundOrderDetail) {
                ++foundOrderDetail.amount;
                let tableBills = allBills.filter(bill => bill.idTable === tableId )
                showBillItems(tableBills, null, tableOrderId);
            }
        }
    })

    Quantity.addEventListener("click", function () {
        Caculator.style.display = "flex";
        document.calc.txt.value = Quantity.textContent;
        Caculator.setAttribute("status", "quantity");
        ButtonDone.setAttribute("order-detail-id", id);
    })

    ButtonSkip.onclick = function () {
        Caculator.style.display = "none";
        ButtonDone.removeAttribute("order-detail-id");
    }

    ButtonDone.onclick = function (event) {
        if(document.calc.txt.value < 1 ){
            document.calc.txt.value = 1;
        }
        let tableId = parseInt(document.getElementById("bill-board").getAttribute("table-id"));
        let tableOrderId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10);
        let orderDetailId = parseInt(event.target.getAttribute("order-detail-id"));
        let foundBill = allBills.find(bill => bill.id === tableOrderId)
        let tableBills = allBills.filter(bill => bill.idTable === tableId)
        if (foundBill) {
            let caculatorStatus = Caculator.getAttribute("status");
            let foundOrderDetail = foundBill.orderDetails.find(orderDetail => orderDetail.id === orderDetailId);

            switch (caculatorStatus) {
                case 'time-price': {
                    fetchMethod(
                        '/tableOrders/' + tableOrderId + '/time-price',
                        {timePrice: parseFloat(document.calc.txt.value)},
                        'put')
                        .then(res => {
                            if (!VALID_STATUS.includes(res.status)) {
                                // forbidden
                                if (res.status === 405) {
                                    return showToastError("Bạn không có có quyền thực hiện hành động này!");
                                }
                                return showToastError(res.message);
                            }

                            foundBill.tablePrice = parseFloat(document.calc.txt.value);
                            showBillItems(tableBills, null, tableOrderId);
                        })
                    break;
                }

                case 'quantity': {
                    if(foundOrderDetail) {
                        foundBill.isNeedUpdate = true;
                        foundOrderDetail.amount = parseFloat(document.calc.txt.value);
                        showBillItems(tableBills, null, tableOrderId);
                    }
                    break;
                }

                case 'price': {
                    if(foundOrderDetail) {
                        fetchMethod(
                            '/orderDetails/' + orderDetailId + '/update-price',
                            {price: parseFloat(document.calc.txt.value)},
                            'put')
                            .then(res => {
                                if (!VALID_STATUS.includes(res.status)) {
                                    // forbidden
                                    if (res.status === 405) {
                                        return showToastError("Bạn không có có quyền thực hiện hành động này!");
                                    }
                                    return showToastError(res.message);
                                }

                                foundOrderDetail.price = parseFloat(document.calc.txt.value)
                                showBillItems(tableBills, null, tableOrderId);
                            })
                    }
                    break;
                }
            }

            Caculator.style.display = "none";
        }
    }

    ChangePrice.onclick = function () {
        Caculator.style.display = "flex";
        Caculator.setAttribute("status", "price");
        document.calc.txt.value = (ChangePrice.innerText).replaceAll(",", "");
        ButtonDone.setAttribute("order-detail-id", id);
    };

    document.getElementById("product-cart-list").appendChild(RowList);
    ScrollMain2();
}

function showBillItems(tableBills, index, billId = null) {
    let billName = document.getElementById('droplist-label-show');
    let cartList = document.getElementById("product-cart-list");
    let billDropList = document.getElementById('bill-drop-list');

    billDropList.innerHTML = '';
    cartList.innerHTML = '';
    let tableOrder;
    if (billId)
        tableOrder = tableBills.find(bill => bill.id === billId)
    else tableOrder = tableBills[index];

    // add remove event for bill list
    tableBills.forEach(bill => {
        let dropItem = document.createElement('div');
        dropItem.setAttribute('class', 'drop__item');
        dropItem.setAttribute('bill-id', bill.id);
        dropItem.innerHTML = `
        <li class="droplist-label">HD${bill.id}</li>
        <i class="dtop__item-icon fas fa-times" onclick="openModal(this)"></i>
        `
        billDropList.appendChild(dropItem);
        dropItem.addEventListener("click", event => {
            let BillId = parseInt(event.target.parentElement.getAttribute("bill-id"));
            // open bill
            if (event.target.tagName === "LI") {
                showBillItems(tableBills, null, BillId);
            }
        })
    })

    let {beginAt, endAt, id, tablePrice} = tableOrder;

    billName.innerText = "HD" + id;
    billName.setAttribute("bill-id", id);

    AddRowHour(cartList, beginAt, endAt, id, tablePrice);

    tableOrder.orderDetails.forEach(item => {
        showBillProduct(item);
    })

    SetPaymentInfo();
}

function getHourMinute(hours) {
    let roundHours = Math.floor(hours);
    let minutes = (hours - roundHours) * 60;
    let rminutes = (Math.round(minutes) >= 0 && Math.round(minutes) < 10) ? `0${Math.round(minutes)}` : Math.round(minutes);
    return roundHours + " giờ " + rminutes + " phút";
}

async function createBill() {
    let billBoard = document.getElementById('bill-board');
    let tableId = billBoard.getAttribute("table-id");
    let res = await fetchMethod(`/tableOrders/${tableId}/order`, {}, "post");
    console.log(res)
    if(!VALID_STATUS.includes(res.status)) {
        return showToastError(res.message);
    }
    return window.location.reload();
}

// Update bill button
let updateBillBtn = document.getElementById("update-bill-btn");

updateBillBtn.addEventListener("click", () => {
    let tableOrderId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10);
    let data = {
        orderDetails: [],
    };

    let foundBill = allBills.find(bill => bill.id === tableOrderId)

    if (foundBill) {
        foundBill.isNeedUpdate = false;
        foundBill.orderDetails.forEach(item => {
            let {amount, cancelReason, canceled, id, price} = item;
            data.orderDetails.push({
                amount,
                cancelReason,
                canceled,
                id,
                price
            })
        })
    }

    fetchMethod("/tableOrders/" + tableOrderId + "/items", data, "put")
        .then(res => {
            if (!VALID_STATUS.includes(res.status)) {
                return showToastError(res.message)
            }

            return showToastInfor("Update thành công")

        });
})

function addBill() {
    setEmptyBill("Hóa đơn")
    let productCartList = document.getElementById("product-cart-list");
    productCartList.innerHTML = '';
}

function setEmptyBill(BillName = "Hóa đơn 1") {
    let paymentDiv = document.getElementById('get-payment-btn');
    let openBillDiv = document.getElementById('open-bill-btn');
    let billName = document.getElementById('droplist-label-show');
    let totalQuantity = document.getElementById("quantity-order")
    let totalPrice = document.getElementById("total-price")
    let billList = document.getElementById("bill-drop-list");
    let cartList = document.getElementById("product-cart-list");
    cartList.innerHTML = ``;
    billList.innerHTML = ``;

    totalQuantity.innerText = "0";
    totalPrice.innerText = "0";

    paymentDiv.style.display = 'none';
    openBillDiv.style.display = 'flex';
    billName.innerText = BillName;
    billName.removeAttribute("bill-id");
}

async function stopTime() {
    let isPaused = document.getElementById("cell-time").getAttribute("is-paused");
    let billId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10);
    let tableId = parseInt(document.getElementById("bill-board").getAttribute("table-id"), 10)
    let res;
    if (isPaused === "true") {
        res = await fetchMethod(`/tableOrders/${billId}/continueTime`, {}, "post");
    } else {
        res = await fetchMethod(`/tableOrders/${billId}/stopTime`, {}, "post");
    }

    if (!VALID_STATUS.includes(res.status)) {
        return showToastError(res.message)
    }
        await fetchTableOrders();
        let tableBills = allBills.filter(bill => bill.idTable === tableId);
        showBillItems(tableBills, null, billId);
}