<script setup lang="ts">

import EditCategoryModal from "~/components/EditCategoryModal.vue";
import type { Category } from "~/models/Category";

const categories = ref<Category[]>([]);
const name = ref('');
const image = ref('');
const selectedCategory = ref<Category | null>(null);

async function submitForm() {
  await $fetch<Category>("http://127.0.0.1:8080/api/v1/admin/category", {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name: name.value, image: image.value }),
  }).then((res) => {
    alert("Category added succesfully!");
    categories.value.push(res);
  }).catch(() => {
    alert("Error accured! ");
  })
}

function closeEditModal() {
  selectedCategory.value = null;
}

async function updateCategory(category: Category) {
  try {
    const res = await $fetch<Category>("http://127.0.0.1:8080/api/v1/admin/category", {
      method: "PUT",
      credentials: "include",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(category),
    });
    const index = categories.value.indexOf(categories.value.find(c => c.id === category.id));
    categories.value[index] = res;
    alert("Updated successfully!");
    closeEditModal();
  }
  catch (error) {
    console.error(error);
    alert("Unable to update category!");
  }
}

async function removeCategory(category: Category) {
  if (confirm("Do you want to delete category named: " + category.name + " ?"))
    await $fetch("http://127.0.0.1:8080/api/v1/admin/category/" + category.id.toString(), {
      method: "DELETE",
      credentials: "include",
    }).then(() => {
      alert("Removed succesfully!");
      categories.value = categories.value.toSpliced(categories.value.indexOf(category), 1);
    }).catch((err) => {
      alert("Unable to remove! Please try again later!");
      console.error(err);
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

})

</script>


<template>
  <NavbarAdmin />
  <div class="card m-auto mb-4 col"
    style="max-height: fit-content; height: fit-content; max-width: 30%; width: 30%; min-width: 30%;">
    <form class="card-body text-center" @submit.prevent="submitForm">
      <div class="mb-3">
        <label for="name" class="form-label">Name</label>
        <input type="text" id="name" class="form-control" v-model="name" required>
      </div>
      <div class="mb-3">
        <label for="image" class="form-label">Image</label>
        <input id="image" class="form-control" v-model="image" required></input>
      </div>
      <button type="submit" class="btn btn-primary">Add category</button>
    </form>
  </div>
  <div class="mb-4 d-flex row w-100 h-100 align-items-center">
    <div class="card m-auto mb-4 col"
      style="max-height: fit-content; height: fit-content; max-width: 30%; width: 30%; min-width: 30%"
      v-if="categories.length > 0" v-for="category in categories" :key="category.id">
      <div class="card-body text-center">
        <h2 class="card-text">{{ category.name }}</h2>
        <p class="card-text">{{ category.image }}</p>
        <button class="btn btn-secondary" @click="selectedCategory = { ...category }">Update</button>
        <button class="btn btn-danger ms-3" @click="removeCategory(category)">Delete</button>
      </div>
    </div>
  </div>
  <EditCategoryModal v-if="selectedCategory" :category="selectedCategory" @close="closeEditModal"
    @update="updateCategory" />

</template>
