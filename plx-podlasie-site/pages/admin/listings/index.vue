<script setup lang="ts">

import type { Listing } from "~/models/Listing";
import type { Category } from "~/models/Category";

const listings = ref<(Listing[])>([]);
const categories = ref<Category[]>([]);
const loading = ref(true);
const unapprovedFilter = ref(false);

async function removeListing(listing: Listing) {
  if (confirm("Are you sure you want to remove listing " + listing.name + " ?"))
    await $fetch("http://127.0.0.1:8080/api/v1/admin/listing/" + listing.id.toString(), {
      method: "DELETE",
      credentials: "include",
    }).then(() => {
      alert("Listing removed successfully!"); listings.value = listings.value.toSpliced(listings.value.indexOf(listing), 1)
    }).catch((err) => {
      alert("Wasn't able to remove listing!"); console.error(err);
    })
}

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

  const response = await fetch("http://127.0.0.1:8080/api/v1/admin/listing", {
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

async function approve(listing: Listing) {
  document.getElementById("listing" + listing.id)?.setAttribute("disabled", "");
  const response = await fetch(`http://127.0.0.1:8080/api/v1/admin/listing/${listing.id}`, {
    method: "POST",
    credentials: "include",
  }).finally(() => {
    document.getElementById("listing" + listing.id)?.removeAttribute("disabled");
  });
  if (response.ok && response.status === 200) {
    alert("Listing approved!");
    listings.value[listings.value.indexOf(listing)] = { ...listing, approved: true };
  } else if (response.ok && response.status === 403) {
    alert("Session expired! Please log in again!");
    useCookie("loggedIn").value = null;
    navigateTo("/auth/login");
  } else {
    alert("Unable to approve listing. Please try again later!");
    console.error(await response.text());
  }
}
</script>


<template>
  <NavbarAdmin />
  <div v-if="listings.length > 0" class="mb-4 d-flex row w-100 h-100 align-items-center">
    <div class="d-flex aligin-items-center justify-content-center my-3">
      <button class="btn btn-primary mx-2" @click="unapprovedFilter = false">All</button>
      <button class="btn btn-primary mx-2" @click="unapprovedFilter = true">Unapproved</button>
    </div>
    <div class="card m-auto mb-4 col"
      style="max-height: fit-content; height: fit-content; max-width: 30%; width: 30%; min-width: 30%"
      v-if="listings.length > 0" v-for="listing in (unapprovedFilter ? listings.filter(l => !l.approved) : listings)"
      :key="listing.id">
      <div class="card-body text-center">
        <h2 class="card-text">{{ listing.name }}</h2>
        <p class="card-text">{{ listing.description }}</p>
        <p class="card-text">{{ categories.find(c => c.id === listing.categoryId)?.name }}</p>
        <p class="card-text">{{ listing.price }} PLN</p>
        <button :id="'listing' + listing.id.toString()" class="btn btn-secondary" @click="approve(listing)"
          v-if="!listing.approved">Approve</button>
        <button class="btn btn-danger ms-3" @click="removeListing(listing)">Delete</button>
      </div>
    </div>
  </div>
  <h1 v-else-if="!loading">No listings found!</h1>
</template>
