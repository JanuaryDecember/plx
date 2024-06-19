<template>
  <div class="d-flex bg-secondary shadow mb-3">
    <NuxtLink class="btn m-2 text-white" to="/">PLX - Better then OLX</NuxtLink>
    <NuxtLink class="btn btn-secondary m-2 text-white" to="/admin/listings">Listings
    </NuxtLink>
    <NuxtLink class="btn btn-secondary my-2 text-white" to="/admin/categories">Categories management</NuxtLink>
    <div class="ms-auto">
      <button class="btn btn-secondary m-2 text-white" @click="logout">Logout</button>
    </div>
  </div>
</template>

<script lang="ts" setup>

async function logout() {
  useCookie("loggedIn", { sameSite: "lax", secure: true }).value = null;
  useCookie("permitted", { sameSite: "lax", secure: true }).value = null;
  await $fetch('http://127.0.0.1:8080/api/v1/auth/logout', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: "include"
  }).finally(() => navigateTo("/"));
}
</script>
