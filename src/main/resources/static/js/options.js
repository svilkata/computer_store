const portURL = 'https://computerstoreproject.herokuapp.com';
// var portURL = 'http://localhost:8080';

const options = {
    method: 'GET',
    headers: {}
};

options.headers['Content-Type'] = 'application/json';
options.headers['Access-Control-Allow-Origin'] = '*';

export {portURL, options};

// options.body = JSON.stringify(data);

// function request(method, url, data) {
//     const options = {
//         method,
//         headers: {}
//     };
//
//     if (data !== undefined) { //we have data
//         options.headers['Content-Type'] = 'application/json';
//         options.headers['Access-Control-Allow-Origin'] = '*';
//         options.body = JSON.stringify(data);
//     }
//
//     return fetch(url, options)
//         .then(resp => {
//             //the logout does not return a body/response
//             // console.log(resp);
//             if (resp.status === 403) {
//                 return '403';
//             }
//
//             if (resp.status === 409) {
//                 return '409';
//             }
//
//             if (resp.url.endsWith('logout')) {
//                 return resp
//             } else {
//                 return resp.json();
//             }
//         });  //return response = promise
// }
