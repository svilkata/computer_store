const container = $('#displayBasket');
const basketId = $("#basketId").attr('value');

// var portURL = 'http://localhost:8080';
var portURL = 'https://computerstoreproject.herokuapp.com';

fetch(portURL + '/users/basket/viewitems/' + basketId)
    .then((response) => response.json())
    .then((result) => displayBasket(result));

function displayBasket(result) {
    container.empty();

    const headTitle = $('<h2>').addClass('text-center text-white mt-5 greybg').text('.........Items in Basket.......');
    container.append(headTitle);

    const headBasketId = $('<h2>').addClass('text-center text-white mt-5 greybg').attr({'id': 'basketId'}).val(basketId);
    container.append(headBasketId);

    if (result.items.length === 0) {
        container.append(
            $('<h5>').addClass('text-center text-white mt-5 greybg').text('No items in this basket yet!')
        );
        return;
    }

    const totalValue = $('<h4>').addClass('text-center text-white mt-5 greybg')
        .text('Total value: ' + result.totalValue + ' LEVA');
    container.append(totalValue);

    // <form method="post" th:action="@{/pages/admins/change-admin-user}"
    //       className="main-form mx-auto col-md-8 d-flex flex-column justify-content-center">

    const divUpper = $('<div>').addClass('main-form mx-auto col-md-8 d-flex flex-column justify-content-center');
    container.append(divUpper);

    const allItems = $('<div>').addClass('offers row mx-auto d-flex flex-row justify-content-center .row-cols-auto');
    result.items.forEach(item => {
        const product = $('<div>').addClass('offer card col-sm-2 col-md-3  col-lg-3 m-2 p-0')
            .attr({'id': 'each', 'value': item.itemId, 'name': 'itemId'})
            .css('min-width' , '250px');

        const cardTop = $('<div>').addClass('card-img-top-wrapper').css({'height': '15rem'});
        const photo = $('<img>').addClass('card-img-top').attr({'alt': 'image', 'src': item.photoUrl});
        cardTop.append(photo);
        product.append(cardTop);


        const cardBody = $('<div>').addClass('card-body pb-1');
        const title = $('<h5>').addClass('card-title').text('Model: ' + item.model);
        cardBody.append(title);
        product.append(cardBody);

        const detailsUl = $('<ul>').addClass('offer-details list-group list-group-flush');
        const detailsLi = $('<li>').addClass('list-group-item');
        const type = $('<div>').addClass('card-text').append($('<span>').text('Type: ' + item.type));
        const quantity = $('<div>').addClass('card-text')
            .append($('<label>')).text('Quantity buying: ');

        const quantityInput = $('<input>').val(item.quantity).attr({'name': 'quantity', 'type': 'number', 'min': 1});
        quantity.append(quantityInput);

        //If quantity has not changed, we do not call the onChangeQuantity function
        quantityInput.on('change', () =>
            onChangeQuantity(item.itemId, miniFunction()));


        function miniFunction() {
            if (quantityInput.val() < 1) {
                alert('Quantity can not be negative or zero');
                quantityInput.val(1);
            }

            return quantityInput.val();
        }

        const totalPrice = $('<div>').addClass('card-text')
            .append($('<span>').css('font-weight', 'bold')
                .text('Price total: ' + item.sellingPriceForAllQuantity + ' LEVA'));
        detailsLi.append(type, quantity, totalPrice);
        detailsUl.append(detailsLi);
        product.append(detailsUl);

        const btnDetails = $('<a>').addClass('btn btn-link')
            .text('Details')
            .attr('href', '/items/all/' + item.type + '/details/' + item.itemId);
        product.append(btnDetails);

        const btnRemoveFromBasket = $('<button>').addClass('btn btn-link')
            .text('Remove from basket');
        btnRemoveFromBasket.on('click', () => onRemoveItemFromBasket(item.itemId));
        product.append(btnRemoveFromBasket);

        allItems.append(product);
    });

    divUpper.append(allItems);

    const div = $('<div>').addClass('button-holder d-flex');
    const submitButton = $('<a>').addClass('btn btn-info btn-lg').text('Continue to Order')
        .attr('href', portURL + '/users/order/' + basketId);
    div.append(submitButton);
    divUpper.append(div);
}

function onChangeQuantity(itemId, newQuantity) {
    fetch(portURL + '/users/basket/changeOneItemQuantityInBasket/' + basketId + '?itemId=' + itemId + '&newQuantity=' + newQuantity)
        .then((response) => {
            if (response.status === 200) {
                alert('Quantity changed successfully');
            }

            if (response.status === 202) {
                alert('Last quantities of the changed item left!');
            }

            return response.json();
        })
        .then((result) => displayBasket(result));
    // console.log('Changed quantity for item with id: ' + itemId + '. New quantity is: ' + newQuantity);
}

function onRemoveItemFromBasket(itemId) {
    fetch(portURL + '/users/basket/removeOneItemFromBasket/' + basketId + '?itemId=' + itemId)
        .then((response) => {
            return response.json();

        })
        .then((result) => {
            displayBasket(result);
            alert('Item removed from basket successfully');
        });
}