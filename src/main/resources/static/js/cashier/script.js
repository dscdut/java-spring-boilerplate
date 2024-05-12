window.addEventListener('load', function () {
    fetchTables();
    fetchProducts();
    fetchTableOrders();
    ScrollMain2();
    setEmptyBill("Hóa đơn")
    changeCategoriesDiv('table');
});

function ShowTable() {
    let ListMenu = document.getElementById('list-menu');
    let ListTable = document.getElementById('list-table');
    if (document.getElementsByClassName("table-wrap").length > 44) {
        ListTable.style.overflowY = "scroll";
        ListTable.style.overflowx = "hidden";
    }
    ListMenu.style.display = "none";
    ListTable.style.display = "flex";
    DisplayNone("flex");
    changeCategoriesDiv('table');
}

function ShowMenu() {
    let ListMenu = document.getElementById('list-menu');
    let ListTable = document.getElementById('list-table');
    if (document.getElementsByClassName("menu-wrap").length > 28) {
        ListMenu.style.overflowY = "scroll";
        ListMenu.style.overflowx = "hidden";
    }
    ListMenu.style.display = "flex";
    ListTable.style.display = "none";
    DisplayNone("flex");
    changeCategoriesDiv('product');
}

// show product categories or table types
function changeCategoriesDiv(type) {
    let tableTypes = document.querySelector('#table_types');
    let productCategories = document.querySelector('#product_categories');
    if (type === 'table') {
        tableTypes.style.display = 'flex';
        productCategories.style.display = 'none'
        return;
    }
    tableTypes.style.display = 'none';
    productCategories.style.display = 'flex'
}

function DisplayNone(Status) {
    let ListMenuWrap = document.querySelectorAll('.menu-wrap');
    let ListTableWrap = document.querySelectorAll('.table-wrap');
    for (const listMenuWrapElement of ListMenuWrap) {
        listMenuWrapElement.style.display = Status;
    }
    for (const listTableWrapElement of ListTableWrap) {
        listTableWrapElement.style.display = Status;
    }
}

function ScrollMain2() {
    let ListProductCart = document.getElementsByClassName('product-cart-list');
    let RowInList = document.querySelectorAll('.row-list');
    if (RowInList.length > 8) {
        ListProductCart[0].style.overflowX = 'hidden';
        ListProductCart[0].style.overflowY = 'scroll';
    }
}

let click = true;

function BillDrop() {
    let billDrop = document.getElementById('bill-drop');
    if (click === true) {

        billDrop.style.display = "flex";
        let ul = document.getElementById('bill-drop-list');
        ul.onclick = function (event) {
            billDrop.style.display = "none";
        }
        click = false;
    } else {
        billDrop.style.display = "none";
        click = true;
    }

}

function SetPaymentInfo() {
    let totalPrice = 0;
    let totalAmount = 0;

    let rowList = document.querySelectorAll("#product-cart-list .row-list");
    for (let rowListElement of rowList) {
        let quantityElement = rowListElement.querySelector(".item-amount");
        let totalElement = rowListElement.querySelector(".item-total");

        totalAmount += parseFloat(quantityElement.innerText);

        let price = totalElement.innerText.replaceAll(",", "");
        totalPrice += parseFloat(price);
    }

    document.getElementById("quantity-order").textContent = numberWithCommas(totalAmount, 1);
    document.getElementById("total-price").textContent = numberWithCommas(totalPrice);
}


function openCellTimePopover() {
 
    let CellTimePopover =  document.getElementById("cell-time-popover");
    CellTimePopover.style.display = "flex";
    window.onmouseup = function (event) {
        if (!CellTimePopover.contains(event.target)){
            CellTimePopover.style.display = "none";
        }
    }
  
    
}

function PaymentButtonClick() {
    // check update status
    let billId = parseInt(document.getElementById('droplist-label-show').getAttribute('bill-id'))

    let foundBill = allBills.find(bill => bill.id === billId);
    if(foundBill.isNeedUpdate === true) return showToastWarning('Hoá đơn chưa được cập nhật!')

    let TimeClickPayment = document.getElementById("time-click-payment");
    TimeClickPayment.textContent = getDateTime(new Date());
    // Tên hóa đơn
    let NameBill = document.getElementById("droplist-label-show");
    // Set tên hóa đơn vào màn hình thanh Toán
    let TitlePayment = document.querySelector(".title-payment span");
    TitlePayment.textContent = NameBill.textContent;

    // Số lượng order
    let QuantityOrder = document.getElementById("quantity-order");
    let QuantityOrderPayment = document.getElementById("quantity-order-payment");
    QuantityOrderPayment.textContent = QuantityOrder.textContent;

    // Tổng tiền
    let TotalPrice = document.getElementById("total-price");
    let TotalPricePayment = document.getElementById("total-price-payment");
    TotalPricePayment.textContent = TotalPrice.textContent;

    let givenMoney = document.getElementById('payingAmountTxt');
    givenMoney.value = TotalPrice.textContent;

    let restMoney = document.getElementById('price-return');
    restMoney.innerText = '0';
    // Tên bàn
    let NameTable = document.querySelector(".main2__header .table-info span");
    let NameTalbePayment = document.getElementById("name-table-payment");
    NameTalbePayment.textContent = NameTable.textContent;

    let PriceNeedToPay = document.getElementById("price-need-to-pay");
    PriceNeedToPay.textContent = numberWithCommas(TotalPrice.textContent);

    // show Bill Items
    let listItems = document.querySelectorAll('#product-cart-list .row-list');
    let paymentItems = document.getElementById('payment-item');
    paymentItems.innerHTML = '';


    listItems.forEach(item => {
        let itemOrder = item.querySelector('.cell-order').innerText;
        let itemName = item.querySelector('.cell-name').innerText;
        let itemAmount = item.querySelector('.item-amount').innerText;
        let itemPrice = item.querySelector('.btn-change-price').innerText;
        let itemTotal = item.querySelector('.item-total').innerText;
        let tr = document.createElement('div');
        tr.setAttribute('class', 'row-list');

        tr.innerHTML = `
        <div class="cell-order">
            <p>${itemOrder}</p>
        </div>
        <div class="cell-name">
            <p>${itemName}</p>
        </div>
        <div class="cell-quantity">
            <p>${itemAmount}</p>
        </div>
        <div class="cell-change-price">
            <p>${itemPrice}</p>
        </div>
        <div class="cell-price">
            <p>${itemTotal}</p>
        </div>
    `
        paymentItems.appendChild(tr);
    })

    //  Mở màn hình thanh toán
    let PaymentDisplay = document.getElementsByClassName("payment");
    PaymentDisplay[0].style.display = "flex";
}

