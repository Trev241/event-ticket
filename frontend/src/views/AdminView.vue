<template>
  <div class="admin-view">
    <!-- Header Section -->
    <section class="header-section text-white py-5">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-8">
            <h1 class="display-4 fw-bold mb-3">
              <i class="fas fa-cog me-3"></i>Admin Dashboard
            </h1>
            <p class="lead">Manage users, events, and tickets</p>
          </div>
          <div class="col-lg-4 text-lg-end">
            <div class="d-flex gap-2 justify-content-lg-end">
              <button class="btn btn-outline-light" @click="refreshData">
                <i class="fas fa-sync-alt me-2"></i>Refresh All
              </button>
              <router-link to="/create-event" class="btn btn-success">
                <i class="fas fa-plus me-2"></i>New Event
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Admin Content -->
    <section class="content-section py-5">
      <div class="container">
        <!-- Stats Overview -->
        <div class="row g-4 mb-5">
          <div class="col-md-3">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-users text-primary mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.totalUsers }}</h3>
                <p class="text-muted mb-0">Total Users</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-calendar-alt text-info mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.totalEvents }}</h3>
                <p class="text-muted mb-0">Total Events</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-ticket-alt text-success mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.totalTickets }}</h3>
                <p class="text-muted mb-0">Total Tickets</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-dollar-sign text-warning mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">${{ stats.totalRevenue }}</h3>
                <p class="text-muted mb-0">Total Revenue</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Tabs Navigation -->
        <ul class="nav nav-tabs mb-4" id="adminTabs" role="tablist">
          <li class="nav-item" role="presentation">
            <button 
              class="nav-link active" 
              id="users-tab" 
              data-bs-toggle="tab" 
              data-bs-target="#users" 
              type="button"
            >
              <i class="fas fa-users me-2"></i>Users
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button 
              class="nav-link" 
              id="events-tab" 
              data-bs-toggle="tab" 
              data-bs-target="#events" 
              type="button"
            >
              <i class="fas fa-calendar-alt me-2"></i>Events
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button 
              class="nav-link" 
              id="tickets-tab" 
              data-bs-toggle="tab" 
              data-bs-target="#tickets" 
              type="button"
            >
              <i class="fas fa-ticket-alt me-2"></i>Tickets
            </button>
          </li>
        </ul>

        <!-- Tab Content -->
        <div class="tab-content" id="adminTabsContent">
          <!-- Users Tab -->
          <div class="tab-pane fade show active" id="users" role="tabpanel">
            <div class="card">
              <div class="card-header bg-primary text-white">
                <h4 class="mb-0">
                  <i class="fas fa-users me-2"></i>User Management
                </h4>
              </div>
              <div class="card-body">
                <div v-if="loadingUsers" class="text-center py-4">
                  <div class="spinner-border text-primary"></div>
                  <p class="mt-2 text-muted">Loading users...</p>
                </div>
                <div v-else-if="users.length === 0" class="text-center py-4">
                  <i class="fas fa-users text-muted mb-3" style="font-size: 3rem;"></i>
                  <h5 class="text-muted">No users found</h5>
                </div>
                <div v-else class="table-responsive">
                  <table class="table table-hover">
                    <thead class="table-dark">
                      <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="user in users" :key="user.id">
                        <td>{{ user.id }}</td>
                        <td>
                          <i class="fas fa-user me-2"></i>
                          {{ user.username }}
                        </td>
                        <td>{{ user.email }}</td>
                        <td>
                          <span 
                            class="badge"
                            :class="user.role === 'admin' ? 'bg-danger' : 'bg-primary'"
                          >
                            {{ user.role }}
                          </span>
                        </td>
                        <td>
                          <button 
                            class="btn btn-sm btn-outline-info"
                            @click="viewUserTickets(user.id)"
                          >
                            <i class="fas fa-ticket-alt me-1"></i>View Tickets
                          </button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- Events Tab -->
          <div class="tab-pane fade" id="events" role="tabpanel">
            <div class="card">
              <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0">
                  <i class="fas fa-calendar-alt me-2"></i>Event Management
                </h4>
                <router-link to="/create-event" class="btn btn-light btn-sm">
                  <i class="fas fa-plus me-1"></i>Create Event
                </router-link>
              </div>
              <div class="card-body">
                <div v-if="loadingEvents" class="text-center py-4">
                  <div class="spinner-border text-info"></div>
                  <p class="mt-2 text-muted">Loading events...</p>
                </div>
                <div v-else-if="events.length === 0" class="text-center py-4">
                  <i class="fas fa-calendar-times text-muted mb-3" style="font-size: 3rem;"></i>
                  <h5 class="text-muted">No events found</h5>
                  <router-link to="/create-event" class="btn btn-primary mt-2">
                    <i class="fas fa-plus me-2"></i>Create First Event
                  </router-link>
                </div>
                <div v-else class="row g-3">
                  <div v-for="event in events" :key="event.id" class="col-lg-6">
                    <div class="event-admin-card card">
                      <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start mb-2">
                          <h5 class="card-title">{{ event.name }}</h5>
                          <span 
                            class="badge"
                            :class="event.availableTickets > 0 ? 'bg-success' : 'bg-danger'"
                          >
                            {{ event.availableTickets > 0 ? 'Available' : 'Sold Out' }}
                          </span>
                        </div>
                        <p class="card-text text-muted">{{ event.description }}</p>
                        <div class="event-details">
                          <small class="text-muted">
                            <i class="fas fa-map-marker-alt me-1"></i>{{ event.venue }}<br>
                            <i class="fas fa-clock me-1"></i>{{ formatDate(event.eventDate) }}<br>
                            <i class="fas fa-ticket-alt me-1"></i>{{ event.availableTickets }} / {{ event.maxTickets }} available<br>
                            <i class="fas fa-dollar-sign me-1"></i>${{ event.ticketPrice }}
                          </small>
                        </div>
                        <div class="mt-3 d-flex gap-2">
                          <router-link 
                            :to="`/events/${event.id}`" 
                            class="btn btn-sm btn-outline-primary"
                          >
                            <i class="fas fa-eye me-1"></i>View
                          </router-link>
                          <button 
                            class="btn btn-sm btn-outline-info"
                            @click="viewEventTickets(event.id)"
                          >
                            <i class="fas fa-list me-1"></i>Tickets
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Tickets Tab -->
          <div class="tab-pane fade" id="tickets" role="tabpanel">
            <div class="card">
              <div class="card-header bg-success text-white">
                <h4 class="mb-0">
                  <i class="fas fa-ticket-alt me-2"></i>Ticket Management
                </h4>
              </div>
              <div class="card-body">
                <div v-if="loadingTickets" class="text-center py-4">
                  <div class="spinner-border text-success"></div>
                  <p class="mt-2 text-muted">Loading tickets...</p>
                </div>
                <div v-else-if="tickets.length === 0" class="text-center py-4">
                  <i class="fas fa-ticket-alt text-muted mb-3" style="font-size: 3rem;"></i>
                  <h5 class="text-muted">No tickets found</h5>
                </div>
                <div v-else class="table-responsive">
                  <table class="table table-hover">
                    <thead class="table-dark">
                      <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Event</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Booking Date</th>
                        <th>Total</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="ticket in tickets" :key="ticket.id">
                        <td>{{ ticket.id }}</td>
                        <td>{{ ticket.user.username }}</td>
                        <td>{{ ticket.event.name }}</td>
                        <td>{{ ticket.quantity }}</td>
                        <td>
                          <span 
                            class="badge"
                            :class="ticket.status === 'CONFIRMED' ? 'bg-success' : 'bg-danger'"
                          >
                            {{ ticket.status }}
                          </span>
                        </td>
                        <td>{{ formatDate(ticket.bookingTime) }}</td>
                        <td>${{ (ticket.quantity * ticket.event.ticketPrice).toFixed(2) }}</td>
                        <td>
                          <button 
                            v-if="ticket.status === 'CONFIRMED'"
                            class="btn btn-sm btn-outline-danger"
                            @click="adminCancelTicket(ticket)"
                            :disabled="cancellingTickets.includes(ticket.id)"
                          >
                            <span v-if="cancellingTickets.includes(ticket.id)" class="spinner-border spinner-border-sm me-1"></span>
                            <i v-else class="fas fa-times me-1"></i>
                            Cancel
                          </button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { authAPI, eventsAPI, ticketsAPI } from '@/services/api'

const users = ref([])
const events = ref([])
const tickets = ref([])

const loadingUsers = ref(false)
const loadingEvents = ref(false)
const loadingTickets = ref(false)
const cancellingTickets = ref([])

const stats = computed(() => {
  const totalRevenue = tickets.value
    .filter(t => t.status === 'CONFIRMED')
    .reduce((sum, t) => sum + (t.quantity * t.event.ticketPrice), 0)

  return {
    totalUsers: users.value.length,
    totalEvents: events.value.length,
    totalTickets: tickets.value.length,
    totalRevenue: totalRevenue.toFixed(2)
  }
})

const loadUsers = async () => {
  loadingUsers.value = true
  try {
    const response = await authAPI.getUsers()
    users.value = response.data
  } catch (err) {
    console.error('Error loading users:', err)
  } finally {
    loadingUsers.value = false
  }
}

const loadEvents = async () => {
  loadingEvents.value = true
  try {
    const response = await eventsAPI.getAll()
    events.value = response.data
  } catch (err) {
    console.error('Error loading events:', err)
  } finally {
    loadingEvents.value = false
  }
}

const loadTickets = async () => {
  loadingTickets.value = true
  try {
    const response = await ticketsAPI.getAll()
    tickets.value = response.data
  } catch (err) {
    console.error('Error loading tickets:', err)
  } finally {
    loadingTickets.value = false
  }
}

const refreshData = () => {
  loadUsers()
  loadEvents()
  loadTickets()
}

const adminCancelTicket = async (ticket) => {
  if (!confirm(`Cancel ticket for ${ticket.user.username}?`)) return

  cancellingTickets.value.push(ticket.id)

  try {
    await ticketsAPI.cancelByAdmin(ticket.id, ticket.user.id)
    
    // Update ticket status locally
    const ticketIndex = tickets.value.findIndex(t => t.id === ticket.id)
    if (ticketIndex !== -1) {
      tickets.value[ticketIndex].status = 'CANCELLED'
    }
  } catch (err) {
    console.error('Error cancelling ticket:', err)
    alert('Failed to cancel ticket')
  } finally {
    cancellingTickets.value = cancellingTickets.value.filter(id => id !== ticket.id)
  }
}

const viewUserTickets = (userId) => {
  // Filter tickets for specific user
  const userTickets = tickets.value.filter(t => t.user.id === userId)
  console.log('User tickets:', userTickets)
  // Could implement a modal or separate view
}

const viewEventTickets = async (eventId) => {
  try {
    const response = await ticketsAPI.getEventTickets(eventId)
    console.log('Event tickets:', response.data)
    // Could implement a modal or separate view
  } catch (err) {
    console.error('Error loading event tickets:', err)
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  refreshData()
})
</script>

<style scoped>
.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.content-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  min-height: 70vh;
}

.stat-card {
  transition: all 0.3s ease;
  border: none;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.1);
}

.nav-tabs .nav-link {
  border: none;
  color: #6c757d;
  font-weight: 500;
}

.nav-tabs .nav-link.active {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border-radius: 10px 10px 0 0;
}

.card-header {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border: none;
}

.card-header.bg-info {
  background: linear-gradient(45deg, #17a2b8, #20c997) !important;
}

.card-header.bg-success {
  background: linear-gradient(45deg, #28a745, #20c997) !important;
}

.event-admin-card {
  transition: all 0.3s ease;
  border: none;
}

.event-admin-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}

.table th {
  border-top: none;
  font-weight: 600;
}

.badge {
  font-size: 0.75rem;
  padding: 0.5rem 0.75rem;
}
</style>
