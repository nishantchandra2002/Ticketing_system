// user.js - User specific actions

// Load tickets created by the logged-in user
async function loadUserTickets() {
  const tickets = await apiRequest("/tickets/my");
  const list = document.getElementById("ticket-list");
  list.innerHTML = "";
  tickets.forEach(t => {
    const li = document.createElement("li");
    li.textContent = `${t.title} - ${t.status}`;
    list.appendChild(li);
  });
}

// Create a new ticket
async function createTicket(event) {
  event.preventDefault();
  const title = document.getElementById("ticket-title").value;
  const desc = document.getElementById("ticket-description").value;
  await apiRequest("/tickets", "POST", { title, description: desc });
  alert("Ticket created!");
  loadUserTickets();
}
