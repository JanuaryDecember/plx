<template>
  <div class="container-fluid m-0 p-0">
    <NavbarLoggedIn />
    <div class="d-flex w-100 h-auto align-items-center justify-content-center mb-3">
      <button class="btn btn-secondary me-2" @click="hideUnApproved()">Approved</button>
      <button class="btn btn-secondary ms-2" @click="showUnApproved()">Unapproved</button>
    </div>
    <div class="d-flex w-100 h-auto align-items-center justify-content-center mb-3">
      <button class="btn btn-secondary" @click="openAddListingModal()">Add listing</button>
    </div>
    <div v-if="listings.length !== 0">
      <div v-if="!isUnApproved" class="d-flex row w-100 h-100 align-items-center">
        <div class="card m-auto col mb-4"
          style="max-height: fit-content; height: fit-content; min-width: 30%; width: 30%; max-width: 30%;"
          v-if="listings.filter(l => l.approved).length > 0" v-for="listing in listings.filter(l => l.approved)"
          :key="listing.id">
          <div class="card-body text-center">
            <button class="btn btn-primary" @click="editListing(listing)">Edit</button>
            <button class="btn btn-danger ms-4" @click="removeListing(listing)">Remove</button>
            <h2 class="card-text">{{ listing.name }}</h2>
            <p class="card-text">{{ listing.description }}</p>
            <p class="card-text">{{ categories.find(c => c.id === listing.categoryId)?.name }}</p>
            <p class="card-text">{{ listing.price }} PLN</p>
          </div>
        </div>
        <div v-else class="d-flex w-100 h-100 align-items-center justify-content-center text-center">
          <h1>No approved listings found</h1>
        </div>
      </div>
      <div v-if="isUnApproved" class="d-flex row w-100 h-100 align-items-center">
        <div class="card m-auto col mb-4"
          style="max-height: fit-content; height: fit-content; min-width: 30%; width: 30%; max-width: 30%;"
          v-if="listings.filter(l => !l.approved).length > 0" v-for="listing in listings.filter(l => !l.approved)"
          :key="listing.id">
          <div class="card-body text-center">
            <button class="btn btn-primary" @click="editListing(listing)">Edit</button>
            <button class="btn btn-danger ms-4" @click="removeListing(listing)">Remove</button>
            <h2 class="card-text">{{ listing.name }}</h2>
            <p class="card-text">{{ listing.description }}</p>
            <p class="card-text">{{ categories.find(c => c.id === listing.categoryId)?.name }}</p>
            <p class="card-text">{{ listing.price }} PLN</p>
          </div>
        </div>
        <div v-else class="d-flex w-100 h-100 align-items-center justify-content-center text-center">
          <h1>No not approved listings found</h1>
        </div>
      </div>
    </div>
    <div v-else-if="!loading" class="d-flex w-100 h-100 align-items-center justify-content-center text-center">
      <h1>No listings found! You still can add one!</h1>
    </div>
    <EditListingModal v-if="selectedListing" :listing="selectedListing" @close="closeEditModal"
      @update="updateListing" />
    <AddListingModal v-if="addListing" :categories="categories" @close="closeAddModal" @add="addNewListing" />
  </div>
</template>

<script lang="ts" setup>
import type { Listing } from "~/models/Listing";
import type { Category } from "~/models/Category";

const addListing = ref(false);
const isUnApproved = ref(false);
const listings = ref<Listing[]>([]);
const categories = ref<Category[]>([]);
const loading = ref(true);
const selectedListing = ref<Listing | null>(null);

function showUnApproved() {
  isUnApproved.value = true;
}

function hideUnApproved() {
  isUnApproved.value = false;
}

function closeAddModal() {
  addListing.value = false;
}

function openAddListingModal() {
  addListing.value = true;
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

  const response = await fetch("http://127.0.0.1:8080/api/v1/premium/listing", {
    method: 'GET',
    credentials: "include"
  });
  if (response.ok && response.status === 200) {
    const data: Listing[] = await response.json();
    listings.value = data;
  } else if (response.status === 403) {
    alert("You need to be user premium to manage listings!");
  } else {
    alert("Unable retrieve data! Please try again later!")
  }
  loading.value = false;
});

const editListing = (listing: Listing) => {
  selectedListing.value = { ...listing };
};

const closeEditModal = () => {
  selectedListing.value = null;
};

async function addNewListing(newListing: {
  name: string,
  description: string,
  price: number,
  image: string,
  categoryId: number,
  userId: number
}) {
  const response = await fetch("http://127.0.0.1:8080/api/v1/premium/listing", {
    method: 'POST',
    credentials: "include",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(newListing)
  });
  if (response.ok && response.status === 200) {
    const addedListing = await response.json();
    listings.value.push(addedListing);
    alert("New listing added successfully!");
    closeAddModal();
  } else if (response.status === 403) {
    alert("You need to be user premium in order to add listing!");
    closeAddModal();
  } else {
    alert("Unable to add listing! Please try again later.");
    closeAddModal();
  }
}

async function removeListing(listing: Listing) {
  if (confirm("Are you sure you want to remove listing?")) {
    const response = await fetch(`http://127.0.0.1:8080/api/v1/premium/listing/${listing.id}`, {
      method: "DELETE",
      credentials: "include",
    })
    if (response.ok && response.status === 200) {
      alert("Listing removed successfully!");
      listings.value = listings.value.toSpliced(listings.value.indexOf(listing), 1);
    } else if (response.status === 403) {
      alert("You need to be user premium in order to remove listing!");
    } else {
      alert("Unable to remove listing! Please try again later.");
      console.error(await response.text());
    }
  }
}

const updateListing = async (updatedListing: Listing) => {
  const response = await fetch('http://127.0.0.1:8080/api/v1/premium/listing', {
    method: 'PUT',
    credentials: "include",
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updatedListing)
  });
  if (response.status === 200) {
    const data: Listing = await response.json();
    const index = listings.value.findIndex(l => l.id === data.id);
    if (index !== -1) {
      listings.value[index] = data;
    }
    closeEditModal();
  } else if (response.status === 403) {
    alert("Session expired! Please log in again!");
    useCookie("loggedIn").value = null;
    navigateTo("/auth/login");
  } else {
    alert(await response.text());
  }

};
</script>
