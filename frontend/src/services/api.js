import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/event-ticket-1.0-SNAPSHOT/api'

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Add token to requests if available
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Handle response errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/users/login', credentials),
  register: (userData) => api.post('/users/register', userData),
  getUsers: () => api.get('/users')
}

// Events API
export const eventsAPI = {
  getAll: () => api.get('/events'),
  getById: (id) => api.get(`/events/${id}`),
  getAvailable: () => api.get('/events/available'),
  create: (eventData) => api.post('/events', eventData)
}

// Tickets API
export const ticketsAPI = {
  book: (bookingData) => api.post('/tickets/book', bookingData),
  getAll: () => api.get('/tickets'),
  getUserTickets: () => api.get('/tickets/user'),
  getEventTickets: (eventId) => api.get(`/tickets/event/${eventId}`),
  cancel: (ticketId) => api.delete(`/tickets/${ticketId}/cancel`),
  cancelByAdmin: (ticketId, userId) => api.delete(`/tickets/${ticketId}/cancel/${userId}`)
}

export default api
