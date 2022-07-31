const allItems = $("#items");
const basketId = $("#basketId").attr('value');
const basketTotalValue = $("#basketTotalValue");


fetch('http://localhost:8080/users/basket/viewitems/' + basketId)
    .then((response) => response.json())
    .then((result) => {
        basketTotalValue.text('Total value: ' + result.totalValue + ' LEVA');
        result.items.forEach(item => {
            const product = $('<div>').addClass('offer card col-sm-2 col-md-3  col-lg-3 m-2 p-0').attr('id', 'each');

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
            const quantity = $('<div>').addClass('card-text').append($('<span>').text('Quantity buying: ' + item.quantity));
            const totalPrice = $('<div>').addClass('card-text')
                .append($('<span>').css('font-weight', 'bold').text('Price total: ' + item.sellingPriceForAllQuantity + ' LEVA'));
            detailsLi.append(type, quantity, totalPrice);
            detailsUl.append(detailsLi);
            product.append(detailsUl);

            const btnDetails = $('<a>').addClass('btn btn-link').text('Details').attr('href', '/items/all/' + item.type + '/details/' + item.itemId);
            product.append(btnDetails);

            allItems.append(product);
        })

    });