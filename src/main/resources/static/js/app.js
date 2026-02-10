 function showView(view){
    const views = document.querySelectorAll(".view");
    views.forEach(v => v.classList.remove("active"));

    if(view === "login") document.getElementById("login-view").classList.add("active");
    if(view === "register") document.getElementById("register-view").classList.add("active");
    if(view === "tasks") {
    document.getElementById("tasks-view").classList.add("active");
    fetchTasks();
    }
}

// ------------------ NOTIFICATION ------------------
function showNotification(message, duration = 2000, type = "success") {
    const notification = document.getElementById("notification");
    notification.textContent = message;
    notification.style.backgroundColor = type === "success" ? "#4caf50" : "#f44336";
    notification.classList.add("show");
    setTimeout(() => notification.classList.remove("show"), duration);
}

// ------------------ LOGIN ------------------
const loginForm = document.getElementById("login-form");
loginForm.addEventListener("submit", async e => {
    e.preventDefault();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    const res = await fetch("/api/auth/login", {
    method:"POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ 
        "userName":username, 
        "password":password })
    });

    if(res.ok){
    showNotification("Login successful");
    
    showView("tasks");
    } else {
    const data = await res.json().catch(()=>({message:"Login failed"}));
    showNotification(data.message,2000,"error");
    }

    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
});

// ------------------ REGISTER ------------------
const registerForm = document.getElementById("register-form");
registerForm.addEventListener("submit", async e => {
    e.preventDefault();
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const userName = document.getElementById("userName").value.trim();
    const password = document.getElementById("reg-password").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();

    if(!firstName || !lastName || !userName || !password || !confirmPassword){
    showNotification("All fields are required",2000,"error");
    return;
    }

    if(password !== confirmPassword){
    showNotification("Passwords do not match",2000,"error");
    return;
    }

    const res = await fetch("/api/auth/register", {
    method:"POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ firstName,lastName,userName,password })
    });

    if(res.ok){
    showNotification("Registration successful");
    showView("login");
    } else {
    const data = await res.json().catch(()=>({message:"Registration failed"}));
    showNotification(data.message,2000,"error");
    }

    document.getElementById("firstName").value = "";
    document.getElementById("lastName").value = "";
    document.getElementById("userName").value = "";
    document.getElementById("reg-password").value = "";
    document.getElementById("confirmPassword").value = "";
});

// ------------------ TASKS ------------------
const taskTitle = document.getElementById("task-title");
const taskDesc = document.getElementById("task-desc");
const taskList = document.getElementById("task-list");
const addTaskBtn = document.getElementById("add-task-btn");
const dropDown = document.getElementById("dropDown");

const apiUrl = "/api/tasks";

// Filter tasks based on dropdown
dropDown.addEventListener("change",()=>{
    if(dropDown.value === "Completed") completedTasks();
    else if(dropDown.value === "pending") pendingTasks();
    else fetchTasks();
});

// Fetch all tasks
async function fetchTasks(){
    const res = await fetch(apiUrl);
    const tasks = await res.json();
    if (!tasks || tasks.length === 0) {
        showNotification("No tasks available ",2000,"error");
    } 
    renderTasks(tasks);
}

// Fetch completed tasks
async function completedTasks(){
    const res = await fetch("/api/tasks/completed");
    const tasks = await res.json();
    if (!tasks || tasks.length === 0) {
        showNotification("No completed tasks ",2000,"error");
    }
    
    renderTasks(tasks);
}

// Fetch pending tasks
async function pendingTasks(){
    const res = await fetch("/api/tasks/pending");
    const tasks = await res.json();
    if (!tasks || tasks.length === 0) {
        showNotification("No pending tasks ",2000,"error");
    }
    renderTasks(tasks);
}

// Render tasks
function renderTasks(tasks){
    taskList.innerHTML = "";
    tasks.reverse().forEach(task=>{
    const li = document.createElement("li");
    li.className = "task-item";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.completed;
    checkbox.addEventListener("change",()=> toggleCompleted(task, checkbox.checked));

    const infoDiv = document.createElement("div");
    infoDiv.className = "task-info" + (task.completed?" completed":"");

    const titleRow = document.createElement("div");
    titleRow.className = "title-row";
    const titleInput = document.createElement("input");
    titleInput.type = "text";
    titleInput.value = task.title;
    titleInput.className = "task-title-input";

    const updateBtn = document.createElement("button");
    updateBtn.textContent = "Update";
    updateBtn.className = "update-btn";

    const descRow = document.createElement("div");
    descRow.className = "desc-row";
    const descInput = document.createElement("input");
    descInput.type = "text";
    descInput.value = task.description;
    descInput.className = "task-desc-input";

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.className = "delete-btn";

    updateBtn.addEventListener("click",()=> updateTask(task.id, titleInput.value, descInput.value, checkbox.checked));
    deleteBtn.addEventListener("click",()=> deleteTask(task.id, li));

    titleRow.appendChild(titleInput);
    titleRow.appendChild(updateBtn);
    descRow.appendChild(descInput);
    descRow.appendChild(deleteBtn);

    infoDiv.appendChild(titleRow);
    infoDiv.appendChild(descRow);

    li.appendChild(checkbox);
    li.appendChild(infoDiv);

    taskList.appendChild(li);
    });
}

// Toggle completed
async function toggleCompleted(task, completed){
    await updateTask(task.id, task.title, task.description, completed);
    showNotification(`Task marked ${completed?"completed":"pending"}`);
}

// Update task
async function updateTask(id, title, description, completed){
    if(!title.trim() || !description.trim()){
    showNotification("Title & description cannot be empty",2000,"error");
    return;
    }
    await fetch(`${apiUrl}/${id}`,{
    method:"PUT",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ title, description, completed })
    });
    fetchTasks();
    showNotification("Task updated");
}

// Add task
addTaskBtn.addEventListener("click", addTask);
async function addTask(){
    const title = taskTitle.value.trim();
    const description = taskDesc.value.trim();
    if(!title || !description){
    showNotification("Title & description required",2000,"error");
    return;
    }
    await fetch(apiUrl,{
    method:"POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ title, description, completed:false })
    });
    taskTitle.value="";
    taskDesc.value="";
    fetchTasks();
    showNotification("Task added");
}

// Delete task
async function deleteTask(id, li){
    li.classList.add("fade-out");
    setTimeout(async ()=>{
    await fetch(`${apiUrl}/${id}`, { method:"DELETE" });
    li.remove();
    showNotification("Task deleted");
    },300);
}

// Logout
function logout(){
    showView("login");
}

// ------------------ PASSWORD TOGGLE ------------------
function togglePassword() {
    const password = document.getElementById('password');
    password.type = password.type === 'password' ? 'text' : 'password';
}

// ------------------ INIT ------------------
document.addEventListener("DOMContentLoaded",()=> showView("login"));