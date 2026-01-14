const apiUrl = '/api/tasks';

const taskList = document.getElementById('task-list');
const taskTitle = document.getElementById('task-title');
const taskDesc = document.getElementById('task-desc');
const addTaskBtn = document.getElementById('add-task-btn');
const dropDown = document.getElementById("dropDown")

// check the drop down option

dropDown.addEventListener("change",()=>{
  if(dropDown.value =="Completed"){
    completedTasks()
  }
  else if(dropDown.value=="pending"){
    pendingTasks()
  }
  else{
    fetchTasks()
  }

})

// function to render all tasks
 async function fetchTasks(){
  const apiUrl = "/api/tasks";
  const resp= await fetch(apiUrl)
  const tasks = await resp.json();
  renderTasks(tasks)

 }

//  function to render completed tasks
 async function completedTasks(){
  const apiUrl= "/api/tasks/completed"
  const resp= await fetch(apiUrl)
  const tasks = await resp.json();
  renderTasks(tasks)

 }

//  function for pending task

 async function pendingTasks(){
  const apiUrl= "api/tasks/pending"
  const resp= await fetch(apiUrl)

  const tasks =await resp.json();
  renderTasks(tasks)

 }

function renderTasks(tasks) {
  taskList.innerHTML = "";

  tasks.forEach(task => {
    const li = document.createElement("li");
    li.className = "task-item"; // optional, for further styling

    // --- Checkbox ---
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.completed;
    checkbox.className = "task-checkbox"; // for styling if needed
    checkbox.addEventListener("change", () =>
      toggleCompleted(task, checkbox.checked)
    );

    // --- Task info container ---
    const infoDiv = document.createElement("div");
    infoDiv.className = "task-info" + (task.completed ? " completed" : "");

    // --- Title row ---
    const titleRow = document.createElement("div");
    titleRow.className = "title-row";

    const titleInput = document.createElement("input");
    titleInput.required =true;
    titleInput.type = "text";
    titleInput.value = task.title;
    titleInput.className = "task-title-input";

    const updateBtn = document.createElement("button");
    updateBtn.textContent = "Update";
    updateBtn.className = "update-btn"; // matches CSS
    updateBtn.addEventListener("click", () =>
      updateTask(task.id, titleInput.value, descInput.value, checkbox.checked)
    );

    titleRow.appendChild(titleInput);
    titleRow.appendChild(updateBtn);

    // --- Description row ---
    const descRow = document.createElement("div");
    descRow.className = "desc-row";

    const descInput = document.createElement("input");
    descInput.required= true ;
    descInput.type = "text";
    descInput.value = task.description;
    descInput.className = "task-desc-input";

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.className = "delete-btn"; // matches CSS
    deleteBtn.addEventListener("click", () => deleteTask(task.id, li));

    descRow.appendChild(descInput);
    descRow.appendChild(deleteBtn);

    // --- Build structure ---
    infoDiv.appendChild(titleRow);
    infoDiv.appendChild(descRow);

    li.appendChild(checkbox);
    li.appendChild(infoDiv);

    // Add newest at top
    taskList.insertBefore(li, taskList.firstChild);
  });
}

// function to read cookie from browser
function getCookie(name) {
    // Split document.cookie by '; ' to get all cookies
    const cookie = document.cookie
        .split('; ')
        .find(row => row.startsWith(name + '='));
    
    // Return the value after '=' or null if not found
    return cookie ? cookie.split('=')[1] : null;
}

csrfToken = getCookie('XSRF-TOKEN' )
console.log(csrfToken)



 //toggle function
 async function toggleCompleted(task,completed){
  await updateTask(task.id,task.title,task.description,completed);
  showNotification(`task has been marked ${completed ? "completed":"pending"}`)
 }

 async function updateTask(id,title,description,completed){
    if(title.trim() ==="" || description.trim() ===""){
        showNotification("title and description cannot be empty")
        return
    }
    
  await fetch(`${apiUrl}/${id}`,{
    method:"PUT",
    headers:{ "Content-Type": "application/json"
              // 'X-CSRF-TOKEN': csrfToken
     },
    body: JSON.stringify({
      title:title,
      description:description,
      completed:completed
    })
  })
  fetchTasks();
  showNotification("task updated")
 }
//add task

addTaskBtn.addEventListener("click",()=>addTask())

async function addTask(){
    if(taskTitle.value.trim() ==="" || taskDesc.value.trim() ===""){
        showNotification("title and description cannot be empty")
        return;
    }
  const title = taskTitle.value.trim();
  const description = taskDesc.value.trim();
  if (!title) return;

  await fetch(apiUrl, {

    method: "POST",
    headers: { "Content-Type": "application/json"
              //'X-CSRF-TOKEN': csrfToken
     },
    body: JSON.stringify({
      title: title,
      description: description,
      completed: false   // default value here
    })
  });
  taskTitle.value = "";
  taskDesc.value = "";
  fetchTasks();
  showNotification("task added successfully")
}
//delete task
 async function deleteTask(id, li) {
  // Animate fade out first
  li.classList.add("fade-out");

  // Wait for animation to finish (300ms)
  setTimeout(async () => {
    await fetch(`${apiUrl}/${id}`, {
      method: "DELETE",
    });

    // Remove from DOM after deletion
    li.remove();
    showNotification("task has been deleted")
  }, 300);
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





 fetchTasks();

