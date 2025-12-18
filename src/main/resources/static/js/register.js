const apiUrl = "/api/register";


const firstNameIn = document.getElementById("firstName");
const lastNameIn = document.getElementById("lastName");
const userNameIn = document.getElementById("userName");
const passwordIn = document.getElementById("password");
const confirmPasswordIn = document.getElementById("confirmPassword");
const registerBtn = document.getElementById("register-btn")



// registerBtn.addEventListener("click", ()=> createUser())

document.getElementById("registerForm").addEventListener("submit",createUser)



async function createUser(e){
    e.preventDefault();
    const firstName =firstNameIn.value.trim();
    const lastName =lastNameIn.value.trim();
    const userName = userNameIn.value.trim();
    const password = passwordIn.value.trim();
    const confirmPassword = confirmPasswordIn.value.trim();


    if(firstName ==="" || lastName ==="" || userName ==="" || password === "" || confirmPassword === ""){
        const message = "all fields are mandatory"
        showNotification(message,2000,"error")
    }
    else{
        if(passwordIn.value.trim() != confirmPasswordIn.value.trim()){
            const message="confirm password mismatch";
            showNotification(message,2000,"error")
        }
        else{
            const response = await fetch(apiUrl,{
                method:"POST",
                headers:{"Content-Type": "application/json"},
                body: JSON.stringify({
                    firstName : firstName,
                    lastName :lastName,
                    userName : userName,
                    password : password
                })
            })

            if(!response.ok){
                let message = "Registration failed";
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
                setTimeout(() => {
                    window.location.href = '/login';
                }, 1000);
                
            }
        }
    }

    firstNameIn.value ="";
    lastNameIn.value ="";
    userNameIn.value= "";
    passwordIn.value="";
    confirmPasswordIn.value ="";

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


