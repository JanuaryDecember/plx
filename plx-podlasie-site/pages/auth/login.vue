<template>
  <Navbar />
  <div class="d-flex justify-content-center">
    <form @submit.prevent="login" class="w-25 mw-25">
      <div class="mb-3">
        <label for="username" class="form-label fs-5">Username</label>
        <input type="text" v-model="username" id="username" class="form-control" required />
      </div>
      <div class="mb-3">
        <label for="password" class="form-label fs-5">Password</label>
        <input type="password" v-model="password" id="password" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary w-100">Login</button>
      <div v-if="error" class="alert alert-danger mt-3" role="alert">
        {{ error }}
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">

const username = ref('');
const password = ref('');
const error = ref('');

async function login() {
  await $fetch('http://127.0.0.1:8080/api/v1/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    body: JSON.stringify({ username: username.value, password: password.value })
  }).then(async () => {
    const response: string = await $fetch('http://127.0.0.1:8080/api/v1/user/permitted', {
      method: 'GET',
      credentials: 'include',
    });
    useCookie("permitted", { maxAge: 1800, sameSite: "lax", secure: true }).value = response;
    useCookie("loggedIn", { maxAge: 1800, sameSite: "lax", secure: true }).value = "true";
    navigateTo("/user/dashboard");
  }).catch((err) => {
    if (err.statusCode === 401) {
      error.value = "Login failed! Please try again!";
    }
    else
      error.value = "There was an error on the server side. Try again later or contact site's Admin";
  })
}
</script>
