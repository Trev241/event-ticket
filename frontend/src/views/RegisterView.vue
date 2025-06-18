<template>
  <div class="register-view min-vh-100 d-flex align-items-center">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
          <div class="card shadow-lg">
            <div class="card-body p-5">
              <div class="text-center mb-4">
                <i class="fas fa-user-plus text-primary mb-3" style="font-size: 3rem;"></i>
                <h2 class="fw-bold">Create Account</h2>
                <p class="text-muted">Join EventTicket today</p>
              </div>

              <form @submit.prevent="handleRegister">
                <div class="mb-3">
                  <label for="email" class="form-label">
                    <i class="fas fa-envelope me-2"></i>Email Address
                  </label>
                  <input
                    type="email"
                    class="form-control"
                    id="email"
                    v-model="form.email"
                    required
                    placeholder="Enter your email"
                  />
                </div>

                <div class="mb-3">
                  <label for="username" class="form-label">
                    <i class="fas fa-user me-2"></i>Username
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="username"
                    v-model="form.username"
                    required
                    placeholder="Choose a username"
                    minlength="3"
                  />
                </div>

                <div class="mb-3">
                  <label for="password" class="form-label">
                    <i class="fas fa-lock me-2"></i>Password
                  </label>
                  <div class="input-group">
                    <input
                      :type="showPassword ? 'text' : 'password'"
                      class="form-control"
                      id="password"
                      v-model="form.password"
                      required
                      placeholder="Create a password"
                      minlength="6"
                    />
                    <button
                      type="button"
                      class="btn btn-outline-secondary"
                      @click="showPassword = !showPassword"
                    >
                      <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                    </button>
                  </div>
                  <div class="form-text">
                    Password must be at least 6 characters long
                  </div>
                </div>

                <div class="mb-3">
                  <label for="confirmPassword" class="form-label">
                    <i class="fas fa-lock me-2"></i>Confirm Password
                  </label>
                  <input
                    type="password"
                    class="form-control"
                    id="confirmPassword"
                    v-model="confirmPassword"
                    required
                    placeholder="Confirm your password"
                    :class="{ 'is-invalid': confirmPassword && form.password !== confirmPassword }"
                  />
                  <div v-if="confirmPassword && form.password !== confirmPassword" class="invalid-feedback">
                    Passwords do not match
                  </div>
                </div>

                <div class="mb-3">
                  <label for="role" class="form-label">
                    <i class="fas fa-user-tag me-2"></i>Account Type
                  </label>
                  <select class="form-select" id="role" v-model="form.role" required>
                    <option value="user">Regular User</option>
                    <option value="admin">Administrator</option>
                  </select>
                </div>

                <div v-if="error" class="alert alert-danger" role="alert">
                  <i class="fas fa-exclamation-triangle me-2"></i>
                  {{ error }}
                </div>

                <div v-if="success" class="alert alert-success" role="alert">
                  <i class="fas fa-check-circle me-2"></i>
                  {{ success }}
                </div>

                <button
                  type="submit"
                  class="btn btn-primary w-100 mb-3"
                  :disabled="loading || form.password !== confirmPassword"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  <i v-else class="fas fa-user-plus me-2"></i>
                  {{ loading ? 'Creating Account...' : 'Create Account' }}
                </button>

                <div class="text-center">
                  <p class="mb-0">
                    Already have an account?
                    <router-link to="/login" class="text-primary text-decoration-none fw-bold">
                      Sign in here
                    </router-link>
                  </p>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authStore } from '@/stores/auth'

const router = useRouter()

const form = ref({
  email: '',
  username: '',
  password: '',
  role: 'user'
})

const confirmPassword = ref('')
const loading = ref(false)
const error = ref('')
const success = ref('')
const showPassword = ref(false)

const handleRegister = async () => {
  if (form.value.password !== confirmPassword.value) {
    error.value = 'Passwords do not match'
    return
  }

  loading.value = true
  error.value = ''
  success.value = ''

  try {
    const result = await authStore.register(form.value)
    
    if (result.success) {
      success.value = 'Account created successfully! Please sign in.'
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      error.value = result.message
    }
  } catch (err) {
    error.value = 'An unexpected error occurred'
  } finally {
    loading.value = false
  }
}

// Auto-redirect if already logged in
if (authStore.isAuthenticated) {
  router.push(authStore.isAdmin ? '/admin' : '/dashboard')
}
</script>

<style scoped>
.register-view {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  padding: 2rem 0;
}

.register-view::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="white" opacity="0.05"/><circle cx="75" cy="75" r="1" fill="white" opacity="0.05"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
}

.card {
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.form-control:focus,
.form-select:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
}

.btn-primary {
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: none;
  font-weight: 600;
  padding: 12px;
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(45deg, #764ba2, #667eea);
  transform: translateY(-2px);
}

.input-group .btn-outline-secondary {
  border-color: #ced4da;
}

.input-group .btn-outline-secondary:hover {
  background-color: #f8f9fa;
  border-color: #667eea;
  color: #667eea;
}
</style>
