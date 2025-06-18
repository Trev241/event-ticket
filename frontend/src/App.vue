<script setup>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar.vue'
</script>

<template>
  <div id="app">
    <Navbar />
    <main class="container-fluid p-0">
      <RouterView />
    </main>
  </div>
</template>

<style>
body {
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

#app {
  min-height: 100vh;
}

.btn-primary {
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: none;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background: linear-gradient(45deg, #764ba2, #667eea);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0,0,0,0.2);
}

.card {
  border: none;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0,0,0,0.15);
}

.form-control {
  border-radius: 10px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
}

.form-control:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
}

.badge {
  border-radius: 20px;
}

.alert {
  border-radius: 10px;
  border: none;
}
</style>
