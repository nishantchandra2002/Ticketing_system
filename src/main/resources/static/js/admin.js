// admin.js - Admin specific actions

// Load all users
async function loadUsers() {
  const users = await apiRequest("/admin/users");
  const list = document.getElementById("user-list");
  list.innerHTML = "";
  users.forEach(u => {
    const li = document.createElement("li");
    li.innerHTML = `${u.username} (${u.role})
      <button onclick="deleteUser(${u.id})">Delete</button>`;
    list.appendChild(li);
  });
}

// Create new user
async function createUser(event) {
  event.preventDefault();
  const username = document.getElementById("new-username").value;
  const password = document.getElementById("new-password").value;
  const role = document.getElementById("new-role").value;
  await apiRequest("/admin/users", "POST", { username, password, role });
  alert("User created!");
  loadUsers();
}

// Delete user
async function deleteUser(id) {
  await apiRequest(`/admin/users/${id}`, "DELETE");
  alert("User deleted!");
  loadUsers();
}

// Load agents for ticket reassignment
async function loadAgents() {
  const agents = await apiRequest("/admin/agents");
  const select = document.getElementById("agent-select");
  select.innerHTML = "";
  agents.forEach(a => {
    const opt = document.createElement("option");
    opt.value = a.id;
    opt.text = a.username;
    select.appendChild(opt);
  });
}

// Load tickets for reassignment
async function loadTickets() {
  const tickets = await apiRequest("/tickets");
  const tbody = document.getElementById("ticket-table").querySelector("tbody");
  tbody.innerHTML = "";
  tickets.forEach(t => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${t.id}</td>
      <td>${t.title}</td>
      <td>${t.status}</td>
      <td>${t.assignedAgent ? t.assignedAgent.username : "Unassigned"}</td>
      <td><button onclick="reassign(${t.id})">Reassign</button></td>`;
    tbody.appendChild(row);
  });
}

// Reassign ticket to agent
async function reassign(ticketId) {
  const agentId = document.getElementById("agent-select").value;
  await apiRequest(`/admin/tickets/${ticketId}/assign`, "PUT", { agentId });
  alert("Ticket reassigned!");
  loadTickets();
}
