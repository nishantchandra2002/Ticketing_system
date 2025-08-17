// agent.js - Agent specific actions

// Load tickets assigned to the logged-in agent
async function loadAssignedTickets() {
  const tickets = await apiRequest("/tickets/assigned");
  const tbody = document.getElementById("assigned-table").querySelector("tbody");
  tbody.innerHTML = "";
  tickets.forEach(t => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${t.id}</td>
      <td>${t.title}</td>
      <td>${t.status}</td>
      <td>
        <button onclick="updateStatus(${t.id}, 'IN_PROGRESS')">Start</button>
        <button onclick="updateStatus(${t.id}, 'RESOLVED')">Resolve</button>
      </td>`;
    tbody.appendChild(row);
  });
}

// Update ticket status
async function updateStatus(ticketId, newStatus) {
  await apiRequest(`/tickets/${ticketId}/status`, "PUT", { status: newStatus });
  alert("Status updated!");
  loadAssignedTickets();
}
