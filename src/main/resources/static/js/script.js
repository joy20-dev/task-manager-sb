const apiUrl = '/tasks';

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
  const apiUrl = "/tasks";
  const resp= await fetch(apiUrl)
  const tasks = await resp.json();
  renderTasks(tasks)

 }

//  function to render completed tasks
 async function completedTasks(){
  const apiUrl= "/tasks/completed"
  const resp= await fetch(apiUrl)
  const tasks = await resp.json();
  renderTasks(tasks)

 }

//  function for pending task

 async function pendingTasks(){
  const apiUrl= "/tasks/pending"
  const resp= await fetch(apiUrl)

  const tasks =await resp.json();
  renderTasks(tasks)

 }

 function renderTasks(tasks){
  taskList.innerHTML= "";

  tasks.forEach(task => {

    //checkbox creation
    const li = document.createElement("li");
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.completed;

    checkbox.addEventListener("change",()=>toggleCompleted(task,checkbox.checked))
    //infodiv created
    const infoDiv = document.createElement("div");
    infoDiv.className = "task-info"+ (task.completed ? ' completed' : 'pending');
    //title of task
    const textInput = document.createElement("input");
    textInput.type ="text";
    textInput.value = task.title ;
    textInput.style.fontWeight ="bold";
    //description of task
    const descInput = document.createElement("input");
    descInput.type ="text";
    descInput.value = task.description;
    //del btn
    const deleteBtn = document.createElement("button");
    deleteBtn.type="button";
    deleteBtn.textContent="delete Task"
    deleteBtn.addEventListener("click",()=> deleteTask(task.id,li))
    // update btn
    const updateBtn = document.createElement("button")
    updateBtn.type="button"
    updateBtn.textContent="update Task";
    updateBtn.addEventListener("click",()=> updateTask(task.id,textInput.value,descInput.value,checkbox.checked));

    
    infoDiv.appendChild(textInput);
    infoDiv.appendChild(descInput);

    li.appendChild(checkbox);
    li.appendChild(infoDiv);
    li.appendChild(deleteBtn);
    li.appendChild(updateBtn);

    taskList.insertBefore(li,taskList.firstChild);  
    // taskList.appendChild(li)
  });
  

 }

 //toggle function
 async function toggleCompleted(task,completed){
  await updateTask(task.id,task.title,task.description,completed);
  showNotification(`task has been marked ${completed ? "completed":"pending"}`)
 }

 async function updateTask(id,title,description,completed){
  await fetch(`${apiUrl}/${id}`,{
    method:"PUT",
    headers:{ "Content-Type": "application/json" },
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
  const title = taskTitle.value.trim();
  const description = taskDesc.value.trim();
  if (!title) return;

  await fetch(apiUrl, {

    method: "POST",
    headers: { "Content-Type": "application/json" },
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

