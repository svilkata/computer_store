var myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");

var urlencoded;

var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: urlencoded,
    redirect: 'follow'
};

fetch("http://localhost:8080", requestOptions)
    .then(response => response.json())
    .then(result => alert("You have succesfully changed your password!"))
        .catch(error => console.log('error', error));