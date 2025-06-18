<template>
  <div class="events-view">
    <!-- Header Section -->
    <section class="header-section text-white py-5">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-8">
            <h1 class="display-4 fw-bold mb-3">
              <i class="fas fa-calendar-alt me-3"></i>Discover Events
            </h1>
            <p class="lead">Find amazing events happening near you</p>
          </div>
          <div class="col-lg-4 text-lg-end">
            <div class="d-flex gap-2 justify-content-lg-end">
              <button 
                class="btn btn-outline-light"
                @click="showAvailableOnly = !showAvailableOnly"
                :class="{ 'active': showAvailableOnly }"
              >
                <i class="fas fa-filter me-2"></i>
                {{ showAvailableOnly ? 'Show All' : 'Available Only' }}
              </button>
              <button class="btn btn-outline-light" @click="loadEvents">
                <i class="fas fa-sync-alt me-2"></i>Refresh
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Events Section -->
    <section class="events-section py-5">
      <div class="container">
        <!-- Loading State -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;"></div>
          <p class="mt-3 text-muted">Loading events...</p>
        </div>

        <!-- Error State -->
        <div v-else-if="error" class="alert alert-danger" role="alert">
          <i class="fas fa-exclamation-triangle me-2"></i>
          {{ error }}
        </div>

        <!-- No Events -->
        <div v-else-if="filteredEvents.length === 0" class="text-center py-5">
          <i class="fas fa-calendar-times text-muted mb-3" style="font-size: 4rem;"></i>
          <h3 class="text-muted">No events found</h3>
          <p class="text-muted">
            {{ showAvailableOnly ? 'No available events at the moment.' : 'No events have been created yet.' }}
          </p>
        </div>

        <!-- Events Grid -->
        <div v-else class="row g-4">
          <div 
            v-for="event in filteredEvents" 
            :key="event.id" 
            class="col-lg-4 col-md-6"
          >
            <div class="event-card card h-100">
              <div class="card-header bg-primary text-white">
                <div class="d-flex justify-content-between align-items-start">
                  <h5 class="card-title mb-0">{{ event.name }}</h5>
                  <span 
                    class="badge"
                    :class="event.availableTickets > 0 ? 'bg-success' : 'bg-danger'"
                  >
                    {{ event.availableTickets > 0 ? 'Available' : 'Sold Out' }}
                  </span>
                </div>
              </div>
              
              <div class="card-body d-flex flex-column">
                <p class="card-text text-muted mb-3">{{ event.description }}</p>
                
                <div class="event-details mb-3">
                  <div class="detail-item mb-2">
                    <i class="fas fa-map-marker-alt text-primary me-2"></i>
                    <strong>Venue:</strong> {{ event.venue }}
                  </div>
                  <div class="detail-item mb-2">
                    <i class="fas fa-clock text-primary me-2"></i>
                    <strong>Date:</strong> {{ formatDate(event.eventDate) }}
                  </div>
                  <div class="detail-item mb-2">
                    <i class="fas fa-dollar-sign text-primary me-2"></i>
                    <strong>Price:</strong> ${{ event.ticketPrice }}
                  </div>
                  <div class="detail-item mb-2">
                    <i class="fas fa-ticket-alt text-primary me-2"></i>
                    <strong>Available:</strong> {{ event.availableTickets }} / {{ event.maxTickets }}
                  </div>
                </div>

                <div class="progress mb-3" style="height: 8px;">
                  <div 
                    class="progress-bar bg-success" 
                    :style="{ width: (event.availableTickets / event.maxTickets) * 100 + '%' }"
                  ></div>
                </div>

                <div class="mt-auto">
                  <router-link 
                    :to="`/events/${event.id}`" 
                    class="btn btn-primary w-100"
                  >
                    <i class="fas fa-eye me-2"></i>View Details
                  </router-link>
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
import { eventsAPI } from '@/services/api'

const events = ref([])
const loading = ref(false)
const error = ref('')
const showAvailableOnly = ref(false)

const filteredEvents = computed(() => {
  if (showAvailableOnly.value) {
    return events.value.filter(event => event.availableTickets > 0)
  }
  return events.value
})

const loadEvents = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await eventsAPI.getAll()
    events.value = response.data
  } catch (err) {
    if (err.response?.status === 500) {
      error.value = 'Server error occurred while loading events. Please try again later.'
    } else if (err.code === 'NETWORK_ERROR') {
      error.value = 'Network connection error. Please check your internet connection.'
    } else {
      error.value = 'Unable to load events at this time. Please try again later.'
    }
    console.error('Error loading events:', err)
  } finally {
    loading.value = false
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
  loadEvents()
})
</script>

<style scoped>
.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.events-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  min-height: 70vh;
}

.event-card {
  transition: all 0.3s ease;
  border: none;
  overflow: hidden;
}

.event-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
}

.card-header {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border: none;
}

.detail-item {
  font-size: 0.9rem;
}

.progress {
  border-radius: 10px;
  background-color: #e9ecef;
}

.progress-bar {
  border-radius: 10px;
}

.btn-outline-light.active {
  background-color: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.badge {
  font-size: 0.75rem;
  padding: 0.5rem 0.75rem;
}
</style>
