const userNameEl = document.getElementById("username");
const passwordEl = document.getElementById("password");


document.getElementById("login-form").addEventListener("submit",login)


async function login(e) {
    e.preventDefault();
    const username = userNameEl.value;
    const password = passwordEl.value;

    const api_url= "/api/auth/login"

    const response = await fetch(api_url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username: username, password: password })
    });

    if(!response.ok){
        let message = "login failed";
        try{
            const data = await response.json();
            message = data.message;
        }catch(e){
            // non-json response
        }
        showNotification(message,2000,"error");

    }
    else{
        showNotification("user registered",2000,"success");
        // setTimeout(() => {
        //     window.location.href = '/login';
        // }, 1000);
        
    }

    userNameEl.value =""
    passwordEl.value =""

    
}

function showNotification(message, duration = 2000, type = "success") {
  const notification = document.getElementById("notification");
  notification.textContent = message;

  // You can change color based on type
  if(type === "success") notification.style.backgroundColor = "#4caf50";
  else if(type === "error") notification.style.backgroundColor = "#f44336";

  notification.classList.add("show");

  // Remove after duration
  setTimeout(() => {
    notification.classList.remove("show");
  }, duration);
}

// Toggle password visibility
function togglePassword() {
    const password = document.getElementById('password');
    const eye = document.getElementById('eye-icon');
    if(password.type === 'password') {
        password.type = 'text';
        eye.classList.add('show-password');
    } else {
        password.type = 'password';
        eye.classList.remove('show-password');
    }
}






