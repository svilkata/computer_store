const form = document.querySelector("form");
form.addEventListener("submit", () => alert("Order created successfully"))

form.on('submit', () => console.log('Submitted'));