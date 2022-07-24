var myHeaders = new Headers();

var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: urlencoded,
    redirect: 'follow'
};

fetch("http://localhost:8080/users/changepassword", requestOptions)
    .then(response => response.json())
    .then(result => alert("You have succesfully changed your password!"))
        .catch(error => console.log('error', error));