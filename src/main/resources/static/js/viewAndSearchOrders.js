const maincontainer = $('#displayOrders');

//initial load of page
fetch('http://localhost:8080/users/order/viewordersrest')
    .then((response) => response.json())
    .then((result) => displayOrdersInitial(result));

function displayOrdersInitial(result) {

    // const title = $('<h2>').addClass('text-center text-white mt-5 greybg');
    // // const span1 = $('<span>').attr('sec:authorize', "!hasRole(\'ADMIN\') && !hasRole(\'EMPLOYEE_SALES\')").text('.......My Orders.......');
    // // const span2 = $('<span>').attr('sec:authorize', "hasRole('ADMIN') || hasRole('EMPLOYEE_SALES')").text('......All orders.......');
    // const span = $('<span>');

    // let boolResult = [[${#authorization.expression('hasRole("EMPLOYEE_SALES")')}]];
    // debugger;
    // console.log(boolResult);

    // let result = [[${#authorization.expression('hasRole("EMPLOYEE_SALES")')}]];
    //
    // console.log(result);

    // if(boolResult){
    //     span.text("MUHAHA")
    //     // span.text('.......My Orders.......');
    // } else {
    //     span.text("BEEEE")
    //     // span.text('......All orders.......');
    // }
    // title.append(span);
    // maincontainer.append(title);

    if (result.length == 0) {
        const noOrders = $('<h4>').addClass('text-center text-white mt-5 greybg').text('.....No orders available.....');
        maincontainer.append(noOrders);
        return;
    }

    const body = $('<div>');
    result.forEach(order => {
        const container = $('<div>').addClass('container');
        const divLeft = $('<div>').addClass('float-left');
        container.append(divLeft);

        const divBody = $('<div>').addClass('row justify-content-md-center').css('min-height', '38px');
        divLeft.append(divBody);

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
        const spanTotalValue = $('<span>').text('Total value: ' + order.totalValue);
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
            .attr('href', '/users/vieworders/' + order.orderNumber + '/details')
            .text('Details');
        divBody.append(anchorDetails);

        //TODO: the dropdown 3 statuses
        //TODO: to check in JS if the user role is either SALES or ADMIN
        // const dropdownChangeOrderStatus =

        body.append(container);
    });

    maincontainer.append(body);
}



const formSearch = $('#searchOrdersForm');
let searchField = "";

formSearch.on('submit', (e) => {
    e.preventDefault(); //можем и без preventDefault като презаписваме стойността на последното търсене след всяко търсене
    const formData = new FormData(e.target);
    searchField = formData.get('search');
    // console.log(searchField);

    //Both two lines below work
    // $('#searchOrdersForm input[name="search"]').val('my new value');
    // formSearch.find('input[name="search"]').val('my new value');

    // fetch('http://localhost:8080/users/order/searchorders/search=' + searchField)
    //     .then((response) => response.json())
    //     .then((result) => displayBasket(result));
});


// <div>
{/*<div className="container" th:each="o : ${orders}" th:object="${o}">*/
}
{/*    <div className="float-left">*/
}
{/*        <div className="row justify-content-md-center" style="min-height: 38px">*/
}
{/*            <div className="col-md-auto text-dark bg-white" style="min-height: 38px">*/
}
{/*                <span th:text="'Client username: ' + *{username}">Client username:</span></div>*/
}

{/*            <div className="col-md-auto text-dark bg-white" style="min-height: 38px">*/
}
{/*                <span th:text="'Order number: ' + *{orderNumber}">Order number:</span>*/
}
{/*            </div>*/
}
{/*            */
}
// <div className="col-md-auto text-dark bg-white" style="min-height: 38px">
//     <span th:text="'Total items: ' + *{totalItemsInOrder}">Total items:</span>
// </div>
// <div className="col-md-auto text-dark bg-white" style="min-height: 38px">
//     <span th:text="'Total value: '+ *{totalValue} + 'BGN'">Total value:</span>
// </div>

// <div className="w-100"></div>
//
// <div className="col-md-auto text-dark bg-white" style="min-height: 38px">
//     <span th:text="'Created at: ' + *{createdAt}">Order created at:</span>
// </div>

// <div className="col-md-auto text-dark bg-white" style="min-height: 38px"><span
//     th:text="'Order status: '+ *{orderStatus}">Order status:</span>
// </div>

// <a className="col-md-auto btn btn-info"
//    th:href="@{/users/vieworders/{orderNumber}/details (orderNumber=*{orderNumber})}">Details</a>

//                 <a sec:authorize="hasRole('EMPLOYEE_SALES')" className="col-md-auto btn btn-info"
//                    th:href="@{/users/changeorderstatus/{orderNumber} (orderNumber=*{orderNumber})}">Change status</a>
//             </div>
//         </div>
//         <br>
//     </div>
//     <br>
// </div>

















