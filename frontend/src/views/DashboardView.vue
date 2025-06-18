<template>
  <div class="dashboard-view">
    <!-- Header Section -->
    <section class="header-section text-white py-5">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-8">
            <h1 class="display-4 fw-bold mb-3">
              <i class="fas fa-tachometer-alt me-3"></i>My Dashboard
            </h1>
            <p class="lead">Welcome back, {{ authStore.user?.username }}!</p>
          </div>
          <div class="col-lg-4 text-lg-end">
            <button class="btn btn-outline-light" @click="loadTickets">
              <i class="fas fa-sync-alt me-2"></i>Refresh
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Dashboard Content -->
    <section class="content-section py-5">
      <div class="container">
        <!-- Stats Cards -->
        <div class="row g-4 mb-5">
          <div class="col-md-4">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-ticket-alt text-primary mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.totalTickets }}</h3>
                <p class="text-muted mb-0">Total Tickets</p>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-check-circle text-success mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.confirmedTickets }}</h3>
                <p class="text-muted mb-0">Confirmed</p>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="stat-card card text-center">
              <div class="card-body">
                <i class="fas fa-times-circle text-danger mb-3" style="font-size: 2.5rem;"></i>
                <h3 class="fw-bold">{{ stats.cancelledTickets }}</h3>
                <p class="text-muted mb-0">Cancelled</p>
              </div>
            </div>
          </div>
        </div>

        <!-- My Tickets Section -->
        <div class="row">
          <div class="col-12">
            <div class="card">
              <div class="card-header bg-primary text-white">
                <h4 class="mb-0">
                  <i class="fas fa-list me-2"></i>My Tickets
                </h4>
              </div>
              <div class="card-body">
                <!-- Loading State -->
                <div v-if="loading" class="text-center py-5">
                  <div class="spinner-border text-primary"></div>
                  <p class="mt-3 text-muted">Loading your tickets...</p>
                </div>

                <!-- Error State -->
                <div v-else-if="error" class="alert alert-danger" role="alert">
                  <i class="fas fa-exclamation-triangle me-2"></i>
                  {{ error }}
                </div>

                <!-- No Tickets -->
                <div v-else-if="tickets.length === 0" class="text-center py-5">
                  <i class="fas fa-ticket-alt text-muted mb-3" style="font-size: 4rem;"></i>
                  <h4 class="text-muted">No tickets found</h4>
                  <p class="text-muted mb-4">You haven't booked any tickets yet.</p>
                  <router-link to="/events" class="btn btn-primary">
                    <i class="fas fa-calendar-alt me-2"></i>Browse Events
                  </router-link>
                </div>

                <!-- Tickets List -->
                <div v-else class="row g-4">
                  <div 
                    v-for="ticket in tickets" 
                    :key="ticket.id" 
                    class="col-lg-6"
                  >
                    <div class="ticket-card card">
                      <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">{{ ticket.event.name }}</h5>
                        <span 
                          class="badge"
                          :class="ticket.status === 'CONFIRMED' ? 'bg-success' : 'bg-danger'"
                        >
                          {{ ticket.status }}
                        </span>
                      </div>
                      <div class="card-body">
                        <div class="ticket-details">
                          <div class="detail-row">
                            <i class="fas fa-map-marker-alt text-primary me-2"></i>
                            <span>{{ ticket.event.venue }}</span>
                          </div>
                          <div class="detail-row">
                            <i class="fas fa-clock text-primary me-2"></i>
                            <span>{{ formatDate(ticket.event.eventDate) }}</span>
                          </div>
                          <div class="detail-row">
                            <i class="fas fa-ticket-alt text-primary me-2"></i>
                            <span>{{ ticket.quantity }} ticket{{ ticket.quantity > 1 ? 's' : '' }}</span>
                          </div>
                          <div class="detail-row">
                            <i class="fas fa-dollar-sign text-primary me-2"></i>
                            <span>${{ (ticket.quantity * ticket.event.ticketPrice).toFixed(2) }}</span>
                          </div>
                          <div class="detail-row">
                            <i class="fas fa-calendar-plus text-primary me-2"></i>
                            <span>Booked: {{ formatDate(ticket.bookingTime) }}</span>
                          </div>
                        </div>

                        <div class="mt-3 d-flex gap-2">
                          <router-link 
                            :to="`/events/${ticket.event.id}`" 
                            class="btn btn-outline-primary btn-sm flex-fill"
                          >
                            <i class="fas fa-eye me-1"></i>View Event
                          </router-link>
                          <button 
                            v-if="ticket.status === 'CONFIRMED' && isEventInFuture(ticket.event.eventDate)"
                            class="btn btn-outline-danger btn-sm flex-fill"
                            @click="cancelTicket(ticket)"
                            :disabled="cancellingTickets.includes(ticket.id)"
                          >
                            <span v-if="cancellingTickets.includes(ticket.id)" class="spinner-border spinner-border-sm me-1"></span>
                            <i v-else class="fas fa-times me-1"></i>
                            Cancel
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Cancel Confirmation Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="fas fa-exclamation-triangle text-warning me-2"></i>
              Cancel Ticket
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p>Are you sure you want to cancel this ticket?</p>
            <div v-if="ticketToCancel" class="alert alert-info">
              <strong>{{ ticketToCancel.event.name }}</strong><br>
              {{ ticketToCancel.quantity }} ticket(s) - ${{ (ticketToCancel.quantity * ticketToCancel.event.ticketPrice).toFixed(2) }}
            </div>
            <p class="text-muted">This action cannot be undone.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
              Keep Ticket
            </button>
            <button 
              type="button" 
              class="btn btn-danger" 
              @click="confirmCancelTicket"
              :disabled="cancellingTickets.length > 0"
            >
              <span v-if="cancellingTickets.length > 0" class="spinner-border spinner-border-sm me-2"></span>
              <i v-else class="fas fa-trash me-2"></i>
              Cancel Ticket
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ticketsAPI } from '@/services/api'
import { authStore } from '@/stores/auth'

const tickets = ref([])
const loading = ref(false)
const error = ref('')
const cancellingTickets = ref([])
const ticketToCancel = ref(null)

const stats = computed(() => {
  const total = tickets.value.length
  const confirmed = tickets.value.filter(t => t.status === 'CONFIRMED').length
  const cancelled = tickets.value.filter(t => t.status === 'CANCELLED').length
  
  return {
    totalTickets: total,
    confirmedTickets: confirmed,
    cancelledTickets: cancelled
  }
})

const loadTickets = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await ticketsAPI.getUserTickets()
    tickets.value = response.data
  } catch (err) {
    if (err.response?.status === 401) {
      error.value = 'Please sign in to view your tickets.'
    } else {
      error.value = 'Unable to load your tickets at this time. Please try again later.'
    }
    console.error('Error loading tickets:', err)
  } finally {
    loading.value = false
  }
}

const cancelTicket = (ticket) => {
  ticketToCancel.value = ticket
  const modal = new bootstrap.Modal(document.getElementById('cancelModal'))
  modal.show()
}

const confirmCancelTicket = async () => {
  if (!ticketToCancel.value) return

  cancellingTickets.value.push(ticketToCancel.value.id)

  try {
    await ticketsAPI.cancel(ticketToCancel.value.id)
    
    // Update ticket status locally
    const ticketIndex = tickets.value.findIndex(t => t.id === ticketToCancel.value.id)
    if (ticketIndex !== -1) {
      tickets.value[ticketIndex].status = 'CANCELLED'
    }

    // Close modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('cancelModal'))
    modal.hide()
    
    ticketToCancel.value = null
  } catch (err) {
    // Handle specific error cases with user-friendly messages
    const errorMessage = err.response?.data?.error || 'Failed to cancel ticket'

    if (errorMessage.includes('past events')) {
      error.value = 'Cannot cancel tickets for past events.'
    } else if (errorMessage.includes('already cancelled')) {
      error.value = 'This ticket has already been cancelled.'
    } else if (errorMessage.includes('only cancel your own')) {
      error.value = 'You can only cancel your own tickets.'
    } else {
      error.value = 'Unable to cancel ticket at this time. Please try again later.'
    }

    console.error('Error cancelling ticket:', err)
  } finally {
    cancellingTickets.value = cancellingTickets.value.filter(id => id !== ticketToCancel.value?.id)
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    weekday: 'short',
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const isEventInFuture = (eventDate) => {
  return new Date(eventDate) > new Date()
}

onMounted(() => {
  loadTickets()
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

.ticket-card {
  transition: all 0.3s ease;
  border: none;
}

.ticket-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}

.card-header {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border: none;
  color: white !important;
}

.ticket-details {
  font-size: 0.9rem;
}

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.detail-row i {
  width: 20px;
}

.badge {
  font-size: 0.75rem;
  padding: 0.5rem 0.75rem;
}
</style>
