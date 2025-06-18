<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-lg">
    <div class="container">
      <router-link class="navbar-brand fw-bold" to="/">
        <i class="fas fa-ticket-alt me-2"></i>
        EventTicket
      </router-link>
      
      <button 
        class="navbar-toggler" 
        type="button" 
        data-bs-toggle="collapse" 
        data-bs-target="#navbarNav"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/">
              <i class="fas fa-home me-1"></i>Home
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/events">
              <i class="fas fa-calendar-alt me-1"></i>Events
            </router-link>
          </li>
          <li v-if="authStore.isAuthenticated" class="nav-item">
            <router-link class="nav-link" to="/dashboard">
              <i class="fas fa-tachometer-alt me-1"></i>Dashboard
            </router-link>
          </li>
          <li v-if="authStore.isAdmin" class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
              <i class="fas fa-cog me-1"></i>Admin
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" to="/admin">
                  <i class="fas fa-users me-1"></i>Manage Users
                </router-link>
              </li>
              <li>
                <router-link class="dropdown-item" to="/create-event">
                  <i class="fas fa-plus me-1"></i>Create Event
                </router-link>
              </li>
            </ul>
          </li>
        </ul>
        
        <ul class="navbar-nav">
          <li v-if="!authStore.isAuthenticated" class="nav-item">
            <router-link class="nav-link" to="/login">
              <i class="fas fa-sign-in-alt me-1"></i>Login
            </router-link>
          </li>
          <li v-if="!authStore.isAuthenticated" class="nav-item">
            <router-link class="nav-link" to="/register">
              <i class="fas fa-user-plus me-1"></i>Register
            </router-link>
          </li>
          <li v-if="authStore.isAuthenticated" class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
              <i class="fas fa-user me-1"></i>{{ authStore.user?.username }}
              <span v-if="authStore.isAdmin" class="badge bg-warning text-dark ms-1">Admin</span>
            </a>
            <ul class="dropdown-menu">
              <li>
                <a class="dropdown-item" href="#" @click="logout">
                  <i class="fas fa-sign-out-alt me-1"></i>Logout
                </a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { authStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
.navbar-brand {
  font-size: 1.5rem;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-link {
  transition: all 0.3s ease;
}

.nav-link:hover {
  transform: translateY(-2px);
}

.dropdown-menu {
  border: none;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  border-radius: 10px;
}

.dropdown-item {
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
}
</style>
