// const portURL = 'https://computerstoreproject.herokuapp.com';
const portURL = 'https://web-production-08b5.up.railway.app';
// var portURL = 'http://localhost:8080';

const options = {
    method: 'GET',
    headers: {}
};

options.headers['Content-Type'] = 'application/json';
options.headers['Access-Control-Allow-Origin'] = '*';

export {portURL, options};
