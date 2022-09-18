const addAnItemToBasket = $('#additemtobasket');

addAnItemToBasket.on('click', buttonClicked);

var portURL = 'https://computerstoreproject.herokuapp.com';

function buttonClicked() {
    fetch(portURL + '/users/basket/additemtobasket/' + addAnItemToBasket.attr('value'))
        .then((response) => {
            console.log(response);

            if (response.status == '202'){
                alert('You have successfully added the item in your basket!')
            }

            if (response.status == '400'){
                alert('This item is already added to your basket! You can not add a second time this item in your basket!')
            }

            if (response.status == '204'){
                alert('This item has ZERO quantity at the moment! You can not add it in your basket right now!')
            }
        });
}
