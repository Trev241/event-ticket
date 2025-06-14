<script setup></script>

<template>
  <div class="d-flex flex-column justify-content-center align-items-center min-vh-100">
    <div class="container">
      <div class="row">
        <div class="col text-center mb-4">
          <h1>Login</h1>
          <p>Don't have an account? <RouterLink to="/register">Sign up.</RouterLink></p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6 mx-auto">
          <div class="form-floating mb-3">
            <input type="text" class="form-control" id="floatingUsername" placeholder="Username" />
            <label for="floatingUsername">Username</label>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6 mx-auto">
          <div class="form-floating mb-4">
            <input
              type="password"
              class="form-control"
              id="floatingPassword"
              placeholder="Password"
            />
            <label for="floatingPassword">Password</label>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6 mx-auto d-grid">
          <button type="button" class="btn btn-primary">Submit</button>
        </div>
      </div>
    </div>
  </div>
</template>
