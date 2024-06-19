<script setup lang="ts">

import type {Listing} from "~/models/Listing";
import type {Category} from "~/models/Category";

const listings = ref<(Listing[])>([]);
const categories = ref<Category[]>([]);
const loading = ref(true);

onMounted(async () => {
  const categoriesResponse = await fetch("http://127.0.0.1:8080/api/v1/category", {
    method: 'GET',
    credentials: "include"
  });
  if (categoriesResponse.ok && categoriesResponse.status === 200) {
    categories.value = await categoriesResponse.json();
  } else {
    alert("Unable to get list of categories! Please try again later.");
    console.error(await categoriesResponse.text())
  }

  const response = await fetch("http://127.0.0.1:8080/api/v1/listing", {
    method: "GET",
    credentials: "include"
  });
  if (response.ok && response.status === 200) {
    listings.value = await response.json();
    loading.value = false;
  } else if (response.ok && response.status === 403) {
    loading.value = false;
    alert("Session expired! Please log in again!");
    useCookie("loggedIn").value = null;
    navigateTo("/auth/login");
  } else {
    loading.value = false;
    alert("Unable to retrieve data! Please try again later!");
    console.error(await response.text());
  }
})
</script>


<template>
  <NavbarLoggedIn/>
  <div v-if="listings.length > 0" class="mb-4 d-flex row w-100 h-100 align-items-center">
    <div class="card m-auto mb-4 col"
         style="max-height: fit-content; height: fit-content; max-width: 30%; width: 30%; min-width: 30%"
         v-if="listings.filter(l => l.approved).length > 0"
         v-for="listing in listings.filter(l => l.approved)" :key="listing.id">
      <div class="card-body text-center">
        <h2 class="card-text">{{ listing.name }}</h2>
        <p class="card-text">{{ listing.description }}</p>
        <p class="card-text">{{ categories.find(c => c.id === listing.categoryId)?.name }}</p>
        <p class="card-text">{{ listing.price }} PLN</p>
      </div>
    </div>
  </div>
  <h1 v-else-if="!loading">No listings found!</h1>
</template>
