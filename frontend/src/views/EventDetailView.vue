<template>
  <div class="event-detail-view">
    <!-- Loading State -->
    <div v-if="loading" class="min-vh-100 d-flex align-items-center justify-content-center">
      <div class="text-center">
        <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;"></div>
        <p class="mt-3 text-muted">Loading event details...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="min-vh-100 d-flex align-items-center justify-content-center">
      <div class="text-center">
        <i class="fas fa-exclamation-triangle text-danger mb-3" style="font-size: 4rem;"></i>
        <h3 class="text-danger">Error Loading Event</h3>
        <p class="text-muted">{{ error }}</p>
        <router-link to="/events" class="btn btn-primary">
          <i class="fas fa-arrow-left me-2"></i>Back to Events
        </router-link>
      </div>
    </div>

    <!-- Event Details -->
    <div v-else-if="event">
      <!-- Hero Section -->
      <section class="hero-section text-white py-5">
        <div class="container">
          <div class="row align-items-center">
            <div class="col-lg-8">
              <div class="mb-3">
                <router-link to="/events" class="btn btn-outline-light btn-sm">
                  <i class="fas fa-arrow-left me-2"></i>Back to Events
                </router-link>
              </div>
              <h1 class="display-4 fw-bold mb-3">{{ event.name }}</h1>
              <p class="lead">{{ event.description }}</p>
              <div class="d-flex gap-3 flex-wrap">
                <span 
                  class="badge fs-6 px-3 py-2"
                  :class="event.availableTickets > 0 ? 'bg-success' : 'bg-danger'"
                >
                  <i class="fas fa-circle me-2"></i>
                  {{ event.availableTickets > 0 ? 'Available' : 'Sold Out' }}
                </span>
                <span class="badge bg-info fs-6 px-3 py-2">
                  <i class="fas fa-ticket-alt me-2"></i>
                  {{ event.availableTickets }} / {{ event.maxTickets }} tickets left
                </span>
              </div>
            </div>
            <div class="col-lg-4 text-lg-end">
              <div class="price-display">
                <div class="price-label">Ticket Price</div>
                <div class="price-amount">${{ event.ticketPrice }}</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Event Info & Booking -->
      <section class="content-section py-5">
        <div class="container">
          <div class="row g-4">
            <!-- Event Information -->
            <div class="col-lg-8">
              <div class="card">
                <div class="card-header bg-primary text-white">
                  <h4 class="mb-0">
                    <i class="fas fa-info-circle me-2"></i>Event Information
                  </h4>
                </div>
                <div class="card-body">
                  <div class="row g-4">
                    <div class="col-md-6">
                      <div class="info-item">
                        <i class="fas fa-map-marker-alt text-primary me-3"></i>
                        <div>
                          <strong>Venue</strong>
                          <p class="mb-0 text-muted">{{ event.venue }}</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="info-item">
                        <i class="fas fa-clock text-primary me-3"></i>
                        <div>
                          <strong>Date & Time</strong>
                          <p class="mb-0 text-muted">{{ formatDate(event.eventDate) }}</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="info-item">
                        <i class="fas fa-dollar-sign text-primary me-3"></i>
                        <div>
                          <strong>Price per Ticket</strong>
                          <p class="mb-0 text-muted">${{ event.ticketPrice }}</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="info-item">
                        <i class="fas fa-users text-primary me-3"></i>
                        <div>
                          <strong>Capacity</strong>
                          <p class="mb-0 text-muted">{{ event.maxTickets }} total tickets</p>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Availability Progress -->
                  <div class="mt-4">
                    <div class="d-flex justify-content-between mb-2">
                      <span><strong>Ticket Availability</strong></span>
                      <span class="text-muted">
                        {{ event.maxTickets - event.availableTickets }} sold / {{ event.maxTickets }} total
                      </span>
                    </div>
                    <div class="progress" style="height: 12px;">
                      <div 
                        class="progress-bar bg-success" 
                        :style="{ width: (event.availableTickets / event.maxTickets) * 100 + '%' }"
                      ></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Booking Section -->
            <div class="col-lg-4">
              <div class="card sticky-top" style="top: 2rem;">
                <div class="card-header bg-success text-white">
                  <h4 class="mb-0">
                    <i class="fas fa-shopping-cart me-2"></i>Book Tickets
                  </h4>
                </div>
                <div class="card-body">
                  <div v-if="!authStore.isAuthenticated" class="text-center">
                    <i class="fas fa-lock text-muted mb-3" style="font-size: 2rem;"></i>
                    <p class="text-muted mb-3">Please sign in to book tickets</p>
                    <router-link to="/login" class="btn btn-primary w-100">
                      <i class="fas fa-sign-in-alt me-2"></i>Sign In
                    </router-link>
                  </div>

                  <div v-else-if="event.availableTickets === 0" class="text-center">
                    <i class="fas fa-times-circle text-danger mb-3" style="font-size: 2rem;"></i>
                    <h5 class="text-danger">Sold Out</h5>
                    <p class="text-muted">This event is completely sold out</p>
                  </div>

                  <form v-else @submit.prevent="bookTickets">
                    <div class="mb-3">
                      <label for="quantity" class="form-label">
                        <i class="fas fa-ticket-alt me-2"></i>Number of Tickets
                      </label>
                      <select 
                        class="form-select" 
                        id="quantity" 
                        v-model="bookingForm.quantity"
                        required
                      >
                        <option v-for="n in Math.min(5, event.availableTickets)" :key="n" :value="n">
                          {{ n }} ticket{{ n > 1 ? 's' : '' }}
                        </option>
                      </select>
                      <div class="form-text">Maximum 5 tickets per booking</div>
                    </div>

                    <div class="booking-summary mb-3 p-3 bg-light rounded">
                      <div class="d-flex justify-content-between">
                        <span>{{ bookingForm.quantity }} × ${{ event.ticketPrice }}</span>
                        <strong>${{ (bookingForm.quantity * event.ticketPrice).toFixed(2) }}</strong>
                      </div>
                    </div>

                    <div v-if="bookingError" class="alert alert-danger" role="alert">
                      <i class="fas fa-exclamation-triangle me-2"></i>
                      {{ bookingError }}
                      <div v-if="bookingError.includes('already have a ticket')" class="mt-2">
                        <router-link to="/dashboard" class="btn btn-outline-danger btn-sm">
                          <i class="fas fa-tachometer-alt me-1"></i>Go to Dashboard
                        </router-link>
                      </div>
                    </div>

                    <div v-if="bookingSuccess" class="alert alert-success" role="alert">
                      <i class="fas fa-check-circle me-2"></i>
                      {{ bookingSuccess }}
                    </div>

                    <button 
                      type="submit" 
                      class="btn btn-success w-100"
                      :disabled="bookingLoading"
                    >
                      <span v-if="bookingLoading" class="spinner-border spinner-border-sm me-2"></span>
                      <i v-else class="fas fa-credit-card me-2"></i>
                      {{ bookingLoading ? 'Booking...' : 'Book Now' }}
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { eventsAPI, ticketsAPI } from '@/services/api'
import { authStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()

const event = ref(null)
const loading = ref(false)
const error = ref('')

const bookingForm = ref({
  quantity: 1
})
const bookingLoading = ref(false)
const bookingError = ref('')
const bookingSuccess = ref('')

// Helper function to clear error messages after some time
const clearErrorAfterDelay = () => {
  setTimeout(() => {
    bookingError.value = ''
  }, 8000) // Clear error after 8 seconds
}

const loadEvent = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await eventsAPI.getById(route.params.id)
    event.value = response.data
  } catch (err) {
    error.value = 'Event not found or failed to load'
    console.error('Error loading event:', err)
  } finally {
    loading.value = false
  }
}

const bookTickets = async () => {
  bookingLoading.value = true
  bookingError.value = ''
  bookingSuccess.value = ''

  try {
    const bookingData = {
      eventId: event.value.id,
      quantity: parseInt(bookingForm.value.quantity)
    }

    await ticketsAPI.book(bookingData)
    
    bookingSuccess.value = `Successfully booked ${bookingForm.value.quantity} ticket(s)!`
    
    // Refresh event data to update available tickets
    setTimeout(() => {
      loadEvent()
      bookingSuccess.value = ''
    }, 2000)
    
  } catch (err) {
    // Handle specific error cases with user-friendly messages
    const errorMessage = err.response?.data?.error || 'Failed to book tickets'

    if (errorMessage.includes('already have a ticket')) {
      bookingError.value = 'You already have a ticket for this event. You can manage your tickets in your dashboard.'
    } else if (errorMessage.includes('tickets available')) {
      bookingError.value = 'Sorry, there are not enough tickets available for your requested quantity.'
    } else if (errorMessage.includes('past events')) {
      bookingError.value = 'Cannot book tickets for past events.'
    } else if (errorMessage.includes('Maximum 5 tickets')) {
      bookingError.value = 'You can book a maximum of 5 tickets per booking.'
    } else {
      bookingError.value = 'Unable to book tickets at this time. Please try again later.'
    }

    // Clear error message after delay
    clearErrorAfterDelay()

    console.error('Error booking tickets:', err)
  } finally {
    bookingLoading.value = false
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadEvent()
})
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.content-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.price-display {
  text-align: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 1.5rem;
  backdrop-filter: blur(10px);
}

.price-label {
  font-size: 0.9rem;
  opacity: 0.8;
  margin-bottom: 0.5rem;
}

.price-amount {
  font-size: 2.5rem;
  font-weight: bold;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.info-item i {
  margin-top: 0.25rem;
  font-size: 1.2rem;
}

.card-header {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border: none;
}

.card-header.bg-success {
  background: linear-gradient(45deg, #28a745, #20c997) !important;
}

.progress {
  border-radius: 10px;
  background-color: #e9ecef;
}

.progress-bar {
  border-radius: 10px;
}

.booking-summary {
  border: 2px dashed #28a745;
}

.btn-success {
  background: linear-gradient(45deg, #28a745, #20c997);
  border: none;
  font-weight: 600;
  padding: 12px;
}

.btn-success:hover:not(:disabled) {
  background: linear-gradient(45deg, #20c997, #28a745);
  transform: translateY(-2px);
}
</style>
