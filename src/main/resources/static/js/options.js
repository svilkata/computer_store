const portURL = 'https://computerstoreproject.herokuapp.com';
// var portURL = 'http://localhost:8080';

const options = {
    method: 'GET',
    headers: {}
};

options.headers['Content-Type'] = 'application/json';
options.headers['Access-Control-Allow-Origin'] = '*';

export {portURL, options};
