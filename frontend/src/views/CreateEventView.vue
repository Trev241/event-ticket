<template>
  <div class="create-event-view">
    <!-- Header Section -->
    <section class="header-section text-white py-5">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-8">
            <div class="mb-3">
              <router-link to="/admin" class="btn btn-outline-light btn-sm">
                <i class="fas fa-arrow-left me-2"></i>Back to Admin
              </router-link>
            </div>
            <h1 class="display-4 fw-bold mb-3">
              <i class="fas fa-plus-circle me-3"></i>Create New Event
            </h1>
            <p class="lead">Add a new event to the platform</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Create Event Form -->
    <section class="form-section py-5">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-8">
            <div class="card shadow-lg">
              <div class="card-header bg-primary text-white">
                <h4 class="mb-0">
                  <i class="fas fa-calendar-plus me-2"></i>Event Details
                </h4>
              </div>
              <div class="card-body p-5">
                <form @submit.prevent="createEvent">
                  <div class="row g-4">
                    <!-- Event Name -->
                    <div class="col-12">
                      <label for="name" class="form-label">
                        <i class="fas fa-tag me-2"></i>Event Name *
                      </label>
                      <input
                        type="text"
                        class="form-control"
                        id="name"
                        v-model="form.name"
                        required
                        placeholder="Enter event name"
                        maxlength="100"
                      />
                      <div class="form-text">{{ form.name.length }}/100 characters</div>
                    </div>

                    <!-- Description -->
                    <div class="col-12">
                      <label for="description" class="form-label">
                        <i class="fas fa-align-left me-2"></i>Description *
                      </label>
                      <textarea
                        class="form-control"
                        id="description"
                        v-model="form.description"
                        required
                        rows="4"
                        placeholder="Describe your event..."
                        maxlength="500"
                      ></textarea>
                      <div class="form-text">{{ form.description.length }}/500 characters</div>
                    </div>

                    <!-- Venue -->
                    <div class="col-md-6">
                      <label for="venue" class="form-label">
                        <i class="fas fa-map-marker-alt me-2"></i>Venue *
                      </label>
                      <input
                        type="text"
                        class="form-control"
                        id="venue"
                        v-model="form.venue"
                        required
                        placeholder="Event venue/location"
                        maxlength="100"
                      />
                    </div>

                    <!-- Event Date -->
                    <div class="col-md-6">
                      <label for="eventDate" class="form-label">
                        <i class="fas fa-clock me-2"></i>Event Date & Time *
                      </label>
                      <input
                        type="datetime-local"
                        class="form-control"
                        id="eventDate"
                        v-model="form.eventDate"
                        required
                        :min="minDateTime"
                      />
                    </div>

                    <!-- Ticket Price -->
                    <div class="col-md-6">
                      <label for="ticketPrice" class="form-label">
                        <i class="fas fa-dollar-sign me-2"></i>Ticket Price *
                      </label>
                      <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input
                          type="number"
                          class="form-control"
                          id="ticketPrice"
                          v-model.number="form.ticketPrice"
                          required
                          min="0"
                          step="0.01"
                          placeholder="0.00"
                        />
                      </div>
                    </div>

                    <!-- Max Tickets -->
                    <div class="col-md-6">
                      <label for="maxTickets" class="form-label">
                        <i class="fas fa-ticket-alt me-2"></i>Maximum Tickets *
                      </label>
                      <input
                        type="number"
                        class="form-control"
                        id="maxTickets"
                        v-model.number="form.maxTickets"
                        required
                        min="1"
                        max="10000"
                        placeholder="e.g., 100"
                      />
                      <div class="form-text">Maximum number of tickets available</div>
                    </div>
                  </div>

                  <!-- Preview Section -->
                  <div class="mt-5">
                    <h5 class="mb-3">
                      <i class="fas fa-eye me-2"></i>Preview
                    </h5>
                    <div class="preview-card card bg-light">
                      <div class="card-body">
                        <div class="row">
                          <div class="col-md-8">
                            <h5 class="card-title">{{ form.name || 'Event Name' }}</h5>
                            <p class="card-text text-muted">{{ form.description || 'Event description will appear here...' }}</p>
                            <div class="event-details">
                              <div class="detail-item">
                                <i class="fas fa-map-marker-alt text-primary me-2"></i>
                                <span>{{ form.venue || 'Venue' }}</span>
                              </div>
                              <div class="detail-item">
                                <i class="fas fa-clock text-primary me-2"></i>
                                <span>{{ formatPreviewDate(form.eventDate) || 'Event Date' }}</span>
                              </div>
                              <div class="detail-item">
                                <i class="fas fa-ticket-alt text-primary me-2"></i>
                                <span>{{ form.maxTickets || 0 }} tickets available</span>
                              </div>
                            </div>
                          </div>
                          <div class="col-md-4 text-md-end">
                            <div class="price-display">
                              <div class="price-label">Ticket Price</div>
                              <div class="price-amount">${{ form.ticketPrice || '0.00' }}</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Error/Success Messages -->
                  <div v-if="error" class="alert alert-danger mt-4" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    {{ error }}
                  </div>

                  <div v-if="success" class="alert alert-success mt-4" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    {{ success }}
                  </div>

                  <!-- Submit Button -->
                  <div class="mt-4 d-flex gap-3">
                    <button
                      type="submit"
                      class="btn btn-primary btn-lg px-4"
                      :disabled="loading"
                    >
                      <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                      <i v-else class="fas fa-plus me-2"></i>
                      {{ loading ? 'Creating Event...' : 'Create Event' }}
                    </button>
                    <router-link to="/admin" class="btn btn-outline-secondary btn-lg px-4">
                      <i class="fas fa-times me-2"></i>Cancel
                    </router-link>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { eventsAPI } from '@/services/api'

const router = useRouter()

const form = ref({
  name: '',
  description: '',
  venue: '',
  eventDate: '',
  ticketPrice: 0,
  maxTickets: 100
})

const loading = ref(false)
const error = ref('')
const success = ref('')

// Minimum date/time is current date/time
const minDateTime = computed(() => {
  const now = new Date()
  now.setMinutes(now.getMinutes() - now.getTimezoneOffset())
  return now.toISOString().slice(0, 16)
})

const createEvent = async () => {
  loading.value = true
  error.value = ''
  success.value = ''

  try {
    // Validate form
    if (!form.value.name.trim()) {
      throw new Error('Event name is required')
    }
    if (!form.value.description.trim()) {
      throw new Error('Event description is required')
    }
    if (!form.value.venue.trim()) {
      throw new Error('Event venue is required')
    }
    if (!form.value.eventDate) {
      throw new Error('Event date is required')
    }
    if (form.value.ticketPrice < 0) {
      throw new Error('Ticket price cannot be negative')
    }
    if (form.value.maxTickets < 1) {
      throw new Error('Maximum tickets must be at least 1')
    }

    // Check if event date is in the future
    const eventDate = new Date(form.value.eventDate)
    if (eventDate <= new Date()) {
      throw new Error('Event date must be in the future')
    }

    // Prepare event data
    const eventData = {
      name: form.value.name.trim(),
      description: form.value.description.trim(),
      venue: form.value.venue.trim(),
      eventDate: form.value.eventDate + ':00', // Add seconds to match backend format
      ticketPrice: parseFloat(form.value.ticketPrice),
      maxTickets: parseInt(form.value.maxTickets)
    }

    await eventsAPI.create(eventData)
    
    success.value = 'Event created successfully!'
    
    // Redirect to admin page after 2 seconds
    setTimeout(() => {
      router.push('/admin')
    }, 2000)
    
  } catch (err) {
    error.value = err.message || err.response?.data?.error || 'Failed to create event'
    console.error('Error creating event:', err)
  } finally {
    loading.value = false
  }
}

const formatPreviewDate = (dateString) => {
  if (!dateString) return ''
  
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
</script>

<style scoped>
.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.form-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  min-height: 70vh;
}

.card {
  border: none;
}

.card-header {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border: none;
}

.form-control:focus,
.form-select:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
}

.preview-card {
  border: 2px dashed #667eea;
  background: rgba(102, 126, 234, 0.05) !important;
}

.detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.price-display {
  text-align: center;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border-radius: 10px;
  padding: 1rem;
}

.price-label {
  font-size: 0.8rem;
  opacity: 0.9;
  margin-bottom: 0.25rem;
}

.price-amount {
  font-size: 1.5rem;
  font-weight: bold;
}

.btn-primary {
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: none;
  font-weight: 600;
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(45deg, #764ba2, #667eea);
  transform: translateY(-2px);
}

.input-group-text {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border: none;
}
</style>
