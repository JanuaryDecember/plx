<template>
  <Navbar/>
  <div class="d-flex justify-content-center">
    <form @submit.prevent="register" class="w-25 mw-25">
      <div class="mb-3">
        <label for="username" class="form-label fs-4">Username</label>
        <input type="text" v-model="username" id="username"
               class="form-control" required/>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label fs-4">Email</label>
        <input type="text" v-model="email" id="email"
               class="form-control" required/>
      </div>
      <div class="mb-3">
        <label for="password" class="form-label fs-4">Password</label>
        <input type="password" v-model="password" id="password"
               class="form-control" required/>
      </div>
      <div class="mb-3">
        <label for="confirmPassword" class="form-label fs-4">Confirm password</label>
        <input type="password" v-model="confirmPassword" id="confirmPassword"
               class="form-control" required/>
      </div>
      <button type="submit"
              class="btn btn-primary">Register
      </button>
      <div v-if="error" class="alert alert-danger mt-3" role="alert">
        {{ error }}
      </div>
    </form>
  </div>
</template>
<script setup lang="ts">
import Navbar from '~/components/Navbar.vue';

const username = ref('');
const email = ref('')
const password = ref('');
const confirmPassword = ref('');
const error = ref('');

async function register() {
  try {
    if (password.value !== confirmPassword.value) {
      error.value = "Password's are not identical";
      return;
    }

    const requestBody = JSON.stringify({username: username.value, email: email.value, password: password.value});

    const response = await fetch('http://127.0.0.1:8080/api/v1/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: "include",
      body: requestBody
    })

    if (response.ok) {
      alert("Account created successfully!");
      navigateTo("/auth/login");
    }

    if (!response.ok) {
      error.value = await response.text();
    }
  } catch (err) {
    error.value = "Unable to establish connection to server. Try again later or contact site's Admin.";
    console.log(err);
  }
}
</script>