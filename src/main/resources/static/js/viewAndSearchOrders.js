const maincontainer = $('#displayOrders');
const roleValueCustomerOnly = maincontainer.attr('value');

// var portURL = 'http://localhost:8080';
var portURL = 'https://computerstoreproject.herokuapp.com';

//With query selector it only works!!! -  ***цензура*** jQuery не ми работи с preventDefault() !!!
const searchOrdersForm = document.querySelector("#searchOrdersForm");
searchOrdersForm.addEventListener("submit", onSearch);

const titleOrders = $('#titleOrders');

function onSearch(event) {
    event.preventDefault();
    const searchField = new FormData(event.target).get('search')

    fetch(portURL + '/users/order/searchorders?search=' + searchField)
        .then((response) => response.json())
        .then((result) => displayOrders(result));
}

//initial load of page
fetch(portURL + '/users/order/viewordersrest')
    .then((response) => response.json())
    .then((result) => displayOrders(result));


function displayOrders(result) {
    maincontainer.empty();
    maincontainer.append(searchOrdersForm);
    maincontainer.append(titleOrders);

    // console.log(maincontainer.html());
    // debugger;

    if (result.length === 0) {
        const noOrders = $('<h4>').addClass('text-center text-white mt-5 greybg').text('.....No orders available.....');
        maincontainer.append(noOrders);
        return;
    }

    const bodyOrders = $('<div>');
    maincontainer.append(bodyOrders);

    result.forEach(order => {
        const container = $('<div>').addClass('container');
        const divLeft = $('<div>').addClass('float-left');
        container.append(divLeft);
        container.append($('<br>'));
        bodyOrders.append(container);

        const divBody = $('<div>').addClass('row justify-content-md-center').css('min-height', '38px');
        divLeft.append(divBody);
        divLeft.append($('<br>'));

        const divClientName = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanClientName = $('<span>').text('Client username: ' + order.username);
        divClientName.append(spanClientName);
        divBody.append(divClientName);

        const divOrderNumber = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanOrderNumber = $('<span>').text('Order number: ' + order.orderNumber);
        divOrderNumber.append(spanOrderNumber);
        divBody.append(divOrderNumber);

        const divTotalItems = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanTotalItems = $('<span>').text('Total items: ' + order.totalItemsInOrder);
        divTotalItems.append(spanTotalItems);
        divBody.append(divTotalItems);

        const divTotalValue = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanTotalValue = $('<span>').text('Total value: ' + order.totalValue + ' BGN');
        divTotalValue.append(spanTotalValue);
        divBody.append(divTotalValue);

        const div100 = $('<div>').addClass('w-100');
        divBody.append(div100);

        const divCreatedAt = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanCreatedAt = $('<span>').text('Created at: ' + order.createdAt);
        divCreatedAt.append(spanCreatedAt);
        divBody.append(divCreatedAt);

        const divOrderStatus = $('<div>').addClass('col-md-auto text-dark bg-white').css('min-height', '38px');
        const spanOrderStatus = $('<span>').text('Order status: ' + order.orderStatus);
        divOrderStatus.append(spanOrderStatus);
        divBody.append(divOrderStatus);

        const anchorDetails = $('<a>').addClass('col-md-auto btn btn-info')
            .attr('href', '/users/order/' + order.orderNumber + '/details')
            .text('Details');
        divBody.append(anchorDetails);

        if (roleValueCustomerOnly === "true") {
            const formChangeStatus = $('<form>').attr('method', 'GET').addClass('col-md-auto btn btn-info');
            const formLabel = $('<label>').addClass('text-white').text('Change status ');
            if (order.orderStatus === 'DELIVERED') {
                formLabel.text('Final status ');
            }
            formChangeStatus.append(formLabel);

            const formSelect = $('<select>');

            let option1Form = null;
            let option2Form = null;
            let option3Form = null;
            if (order.orderStatus === 'DELIVERED') {
                option3Form = $('<option>').val('DELIVERED').text('DELIVERED');
                option3Form.attr('selected', option3Form.val() === order.orderStatus);
                formSelect.append(option3Form);
            } else if (order.orderStatus === 'CONFIRMED_BY_STORE') {
                option2Form = $('<option>').val('CONFIRMED_BY_STORE').text('CONFIRMED_BY_STORE');
                option2Form.attr('selected', option2Form.val() === order.orderStatus);
                formSelect.append(option2Form);

                option3Form = $('<option>').val('DELIVERED').text('DELIVERED');
                formSelect.append(option3Form);
            } else if (order.orderStatus === 'CONFIRMED_BY_CUSTOMER') {
                option1Form = $('<option>').val('CONFIRMED_BY_CUSTOMER').text('CONFIRMED_BY_CUSTOMER');
                option1Form.attr('selected', option1Form.val() === order.orderStatus);
                formSelect.append(option1Form);

                option2Form = $('<option>').val('CONFIRMED_BY_STORE').text('CONFIRMED_BY_STORE');
                formSelect.append(option2Form);
            }

            formChangeStatus.append(formSelect);
            divBody.append(formChangeStatus);

            formChangeStatus.on('submit', (event) =>
                changeOrderStatus(event, order.orderNumber, order.orderStatus));
            formSelect.on('change', () => {
                // console.log('We changed the select');
                order.orderStatus = $('option:selected', formSelect).val(); //we get here the selected option from the <select> of the current form
                formChangeStatus.submit();
            });
        }//end of if
    });

}

function changeOrderStatus(event, orderNumber, orderStatus) {
    event.preventDefault();
    debugger;
    let searchContent = searchOrdersForm.querySelector('input').value;

    //Normal all offers we show - no search is selected
    fetch(portURL + '/users/order/changestatus/' + orderNumber + '?orderStatus=' + orderStatus + '&search=' + searchContent)
        .then((response) => response.json())
        .then((result) => displayOrders(result));
}
