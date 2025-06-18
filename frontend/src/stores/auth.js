import { reactive } from 'vue'
import { authAPI } from '@/services/api'

export const authStore = reactive({
  user: null,
  token: null,
  isAuthenticated: false,
  isAdmin: false,

  init() {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('user')
    
    if (token && user) {
      this.token = token
      this.user = JSON.parse(user)
      this.isAuthenticated = true
      this.isAdmin = this.user?.role === 'admin'
    }
  },

  async login(credentials) {
    try {
      console.log('Attempting login with:', credentials)
      const response = await authAPI.login(credentials)
      console.log('Login response:', response)
      const token = response.data

      // Decode JWT to get user info (simple decode, not verification)
      const payload = JSON.parse(atob(token.split('.')[1]))
      console.log('JWT payload:', payload)
      const user = {
        username: payload.username,
        role: payload.role,
        userId: payload.userId
      }

      this.token = token
      this.user = user
      this.isAuthenticated = true
      this.isAdmin = user.role === 'admin'

      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))

      return { success: true }
    } catch (error) {
      console.error('Login error:', error)
      return {
        success: false,
        message: error.response?.data || error.message || 'Login failed'
      }
    }
  },

  async register(userData) {
    try {
      await authAPI.register(userData)
      return { success: true }
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data || 'Registration failed' 
      }
    }
  },

  logout() {
    this.user = null
    this.token = null
    this.isAuthenticated = false
    this.isAdmin = false
    
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
})

// Initialize on load
authStore.init()
