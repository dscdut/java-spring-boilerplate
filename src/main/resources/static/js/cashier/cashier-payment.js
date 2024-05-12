function ClosePayment() {
    let Payment = document.getElementsByClassName("payment");
    Payment[0].style.display = "none";
}

// Giảm giá
let Discount = document.querySelector("#discount-price");

// Tiền khách cần trả
let PriceNeedToPay = document.getElementById("price-need-to-pay");

// Tiền trả lại khách
let PriceReturn = document.getElementById("price-return");

let DiscountPopover = document.querySelector(".popover-discount");
function PopoverDiscount() {
    DiscountPopover.style.display = "flex";

    let MessageDiscount = document.querySelector(".form-discount span");
    MessageDiscount.textContent = "";
}

let vndBtn = document.getElementById('vnd-btn');
let percentBtn = document.getElementById('percent-btn');

vndBtn.addEventListener('click', () => {
    PaymentButtonClick()
    let totalPricePayment = parseFloat(document.getElementById("total-price-payment").textContent.replaceAll(',', ''))
    let inputDiscount = parseFloat(document.getElementById("discount-payment").value.replaceAll(',', ''))
    let givenMoney = document.getElementById('payingAmountTxt');
    if ((totalPricePayment - inputDiscount) >= 0) {
        Discount.textContent = numberWithCommas(inputDiscount);
        PriceNeedToPay.textContent = numberWithCommas(totalPricePayment - inputDiscount);
        PriceReturn.textContent = "0";
        givenMoney.value = PriceNeedToPay.textContent;
        DiscountPopover.style.display = "none";
    }
})

percentBtn.addEventListener('click', () => {
    PaymentButtonClick()
    let totalPricePayment = parseFloat(document.getElementById("total-price-payment").textContent.replaceAll(',', ''))
    let inputDiscount = parseFloat(document.getElementById("discount-payment").value.replaceAll(',', ''))
    let givenMoney = document.getElementById('payingAmountTxt');

    if ((totalPricePayment - inputDiscount * totalPricePayment / 100) >= 0) {
        let MessageDiscount = document.querySelector(".form-discount span");
        MessageDiscount.textContent = "(Giảm " + numberWithCommas(inputDiscount) + "%)";
        let discountAmount = inputDiscount * totalPricePayment / 100
        Discount.textContent = numberWithCommas(discountAmount);
        PriceNeedToPay.textContent = numberWithCommas(totalPricePayment - discountAmount);
        givenMoney.value = PriceNeedToPay.textContent;
        PriceReturn.textContent = "0";

        DiscountPopover.style.display = "none";
    }
})

let payBtn = document.getElementById('payment-button');
payBtn.addEventListener('click', async () => {
    let billId = parseInt(document.getElementById('droplist-label-show').getAttribute('bill-id'), 10)
    let tableId = parseInt(document.getElementById('bill-board').getAttribute('table-id'), 10)
    let foundBill = allBills.find(bill => bill.id === billId);
    if (!billId || !tableId || !foundBill) return;

    if (!foundBill.endAt) return showToastWarning("Dừng giờ trước khi thanh toán")

    let discount = parseFloat(document.getElementById('discount-price').innerText.replaceAll(',', ''));
    let data = {
        discount
    }
    let route = `/tableOrders/${billId}/pay`
    let res = await fetchMethod(route, data, "post");
    if (!VALID_STATUS.includes(res.status)) {
        return showToastError(res.message)
    }
    return window.location.reload();
})

function setCommaForInput(target) {
    let rawNumber = target.value.replaceAll(/[^0-9]/g, '');
    let parsed = parseInt(rawNumber);
    if (isNaN(parsed)) return target.value = 0;
    target.value = numberWithCommas(parsed)

    if (target.getAttribute('id') === 'payingAmountTxt') {
        let priceNeedToPay = parseInt(document.getElementById('price-need-to-pay').innerText.replaceAll(',', ''), 10);
        let priceReturn = document.getElementById('price-return');
        priceReturn.innerText = numberWithCommas(parsed - priceNeedToPay)
    }
}


