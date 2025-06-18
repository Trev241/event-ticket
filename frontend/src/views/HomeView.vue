<template>
  <div class="home-view">
    <!-- Hero Section -->
    <section class="hero-section text-white text-center py-5">
      <div class="container">
        <div class="row align-items-center min-vh-100">
          <div class="col-lg-6">
            <h1 class="display-3 fw-bold mb-4">
              Welcome to <span class="text-gradient">EventTicket</span>
            </h1>
            <p class="lead mb-4">
              Discover amazing events, book tickets instantly, and create unforgettable memories. 
              Your gateway to the best events in town!
            </p>
            <div class="d-flex gap-3 justify-content-center">
              <router-link 
                v-if="!authStore.isAuthenticated" 
                to="/register" 
                class="btn btn-primary btn-lg px-4"
              >
                <i class="fas fa-rocket me-2"></i>Get Started
              </router-link>
              <router-link 
                to="/events" 
                class="btn btn-outline-light btn-lg px-4"
              >
                <i class="fas fa-calendar-alt me-2"></i>Browse Events
              </router-link>
            </div>
          </div>
          <div class="col-lg-6">
            <div class="hero-image">
              <i class="fas fa-ticket-alt hero-icon"></i>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section py-5 bg-light">
      <div class="container">
        <div class="row text-center mb-5">
          <div class="col-12">
            <h2 class="display-5 fw-bold mb-3">Why Choose EventTicket?</h2>
            <p class="lead text-muted">Experience the best in event management and ticketing</p>
          </div>
        </div>
        
        <div class="row g-4">
          <div class="col-md-4">
            <div class="feature-card card h-100 text-center p-4">
              <div class="feature-icon mb-3">
                <i class="fas fa-bolt text-primary"></i>
              </div>
              <h4 class="fw-bold mb-3">Instant Booking</h4>
              <p class="text-muted">
                Book your tickets instantly with our fast and secure booking system. 
                No waiting, no hassle.
              </p>
            </div>
          </div>
          
          <div class="col-md-4">
            <div class="feature-card card h-100 text-center p-4">
              <div class="feature-icon mb-3">
                <i class="fas fa-shield-alt text-success"></i>
              </div>
              <h4 class="fw-bold mb-3">Secure Payments</h4>
              <p class="text-muted">
                Your transactions are protected with bank-level security. 
                Pay with confidence.
              </p>
            </div>
          </div>
          
          <div class="col-md-4">
            <div class="feature-card card h-100 text-center p-4">
              <div class="feature-icon mb-3">
                <i class="fas fa-headset text-info"></i>
              </div>
              <h4 class="fw-bold mb-3">24/7 Support</h4>
              <p class="text-muted">
                Our dedicated support team is here to help you anytime, 
                anywhere.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section py-5 text-white">
      <div class="container">
        <div class="row text-center">
          <div class="col-md-3 mb-4">
            <div class="stat-item">
              <h3 class="display-4 fw-bold">{{ stats.events }}+</h3>
              <p class="lead">Events Hosted</p>
            </div>
          </div>
          <div class="col-md-3 mb-4">
            <div class="stat-item">
              <h3 class="display-4 fw-bold">{{ stats.tickets }}+</h3>
              <p class="lead">Tickets Sold</p>
            </div>
          </div>
          <div class="col-md-3 mb-4">
            <div class="stat-item">
              <h3 class="display-4 fw-bold">{{ stats.users }}+</h3>
              <p class="lead">Happy Users</p>
            </div>
          </div>
          <div class="col-md-3 mb-4">
            <div class="stat-item">
              <h3 class="display-4 fw-bold">{{ stats.satisfaction }}%</h3>
              <p class="lead">Satisfaction Rate</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section py-5 bg-light">
      <div class="container text-center">
        <div class="row justify-content-center">
          <div class="col-lg-8">
            <h2 class="display-5 fw-bold mb-4">Ready to Get Started?</h2>
            <p class="lead mb-4">
              Join thousands of event enthusiasts and start your journey today!
            </p>
            <router-link 
              v-if="!authStore.isAuthenticated" 
              to="/register" 
              class="btn btn-primary btn-lg px-5"
            >
              <i class="fas fa-user-plus me-2"></i>Create Account
            </router-link>
            <router-link 
              v-else 
              to="/events" 
              class="btn btn-primary btn-lg px-5"
            >
              <i class="fas fa-calendar-alt me-2"></i>Explore Events
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { authStore } from '@/stores/auth'

const stats = ref({
  events: 150,
  tickets: 5000,
  users: 2500,
  satisfaction: 98
})
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="white" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  opacity: 0.1;
}

.text-gradient {
  background: linear-gradient(45deg, #ffd700, #ffed4e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-icon {
  font-size: 15rem;
  opacity: 0.1;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

.feature-icon i {
  font-size: 3rem;
}

.feature-card {
  transition: all 0.3s ease;
  border: none;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.stats-section {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

.stat-item {
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: scale(1.05);
}

.cta-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}
</style>
