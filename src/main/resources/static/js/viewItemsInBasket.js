const container = $('#displayBasket');
const basketId = $("#basketId").attr('value');

fetch('http://localhost:8080/users/basket/viewitems/' + basketId)
    .then((response) => response.json())
    .then((result) => displayBasket(result));

function displayBasket(result) {
    container.empty();

    const headTitle = $('<h2>').addClass('text-center text-white mt-5 greybg').text('.........Items in Basket.......');
    container.append(headTitle);

    const headBasketId = $('<h2>').addClass('text-center text-white mt-5 greybg').attr({'id': 'basketId'}).val(basketId);
    container.append(headBasketId);

    if (result.items.length == 0) {
        container.append(
            $('<h5>').addClass('text-center text-white mt-5 greybg').text('No items in this basket yet!')
        );
        return;
    }

    const totalValue = $('<h4>').addClass('text-center text-white mt-5 greybg')
        .text('Total value: ' + result.totalValue + ' LEVA');
    container.append(totalValue);

    const form = $('<form>');
    container.append(form);
    const allItems = $('<div>').addClass('offers row mx-auto d-flex flex-row justify-content-center .row-cols-auto');

    result.items.forEach(item => {
        const product = $('<div>').addClass('offer card col-sm-2 col-md-3  col-lg-3 m-2 p-0')
            .attr({'id': 'each', 'value': item.itemId, 'name': 'itemId'});

        const cardTop = $('<div>').addClass('card-img-top-wrapper').css({'height': '20rem'});
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

        const quantityInput = $('<input>').val(item.quantity).attr('name', 'quantity');
        quantity.append(quantityInput);
        //TODO - if quantity has not changed, we should not call the onChangeQuantity function
        //TODO - if new quantity is less than zero
        quantityInput.on('change', () => onChangeQuantity(item.itemId, quantityInput.val()));

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

        allItems.append(product);
    });

    form.append(allItems);
    const div = $('<div>').addClass('button-holder d-flex');
    const submitButton = $('<input/>').attr({'type': 'submit', 'id': 'submit'}).val('Continue to Order');
    submitButton.on('click', onSubmit);
    div.append(submitButton);
    form.append(div);
}

function onChangeQuantity(itemId, newQuantity) {
    fetch('http://localhost:8080/users/basket/changeOneItemQuantityInBasket/' + basketId + '?itemId=' + itemId + '&newQuantity=' + newQuantity)
        .then((response) => {
            if (response.status == 200){
                alert('Quantity changed successfully');
            }

            if (response.status == 202){
                alert('Last quantities of the changed item left!');
            }

            return response.json();
        })
        .then((result) => {
                console.log(result)
                displayBasket(result);
            }
        );
    console.log('Changed quantity for item with id: ' + itemId + '. New quantity is: ' + newQuantity);
    // FETCH here
}

function onSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    console.log(data);
    // FETCH here
}


// <div className="form-group col-md-6 mb-3">
//     <label htmlFor="quantity" className="text-white font-weight-bold">Quantity buying:</label>
//     <div className="card-text">
//         <input id="quantity"
//                th:field="*{quantity}"
//                type="number"
//                className="form-control"
//                placeholder="quantity"
//                required/>
//     </div>
// </div>

// <div className="row">
//     <div className="col col-md-4">
//         <div className="button-holder d-flex">
//             <input type="submit" className="btn btn-info btn-lg" value="Login"/>
//         </div>
//     </div>
// </div>