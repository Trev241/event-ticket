<script setup></script>

<template>
  <div class="d-flex flex-column justify-content-center align-items-center min-vh-100">
    <div class="container">
      <div class="row">
        <div class="col text-center mb-4">
          <h1>Organize an event</h1>
          <p>Schedule a new event for others to join</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6 mx-auto">
          <div class="form-floating mb-3">
            <input type="text" class="form-control" id="floatingTitle" placeholder="Title" />
            <label for="floatingTitle">Title</label>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6 mx-auto">
          <div class="form-floating mb-4">
            <textarea
              class="form-control"
              placeholder="Write a description here"
              id="floatingDescription"
              style="height: 100px"
            ></textarea>
            <label for="floatingDescription">Description</label>
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
