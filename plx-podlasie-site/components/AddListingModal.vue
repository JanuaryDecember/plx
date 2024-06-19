<template>
  <div class="modal fade show" tabindex="-1" style="display: block;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Add Listing</h5>
          <button type="button" class="btn-close" aria-label="Close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Name</label>
              <input type="text" id="name" class="form-control" v-model="newListing.name" required>
            </div>
            <div class="mb-3">
              <label for="description" class="form-label">Description</label>
              <textarea id="description" class="form-control" v-model="newListing.description"
                        required></textarea>
            </div>
            <div class="mb-3">
              <label for="price" class="form-label">Price</label>
              <input type="number" step="0.01" id="price" class="form-control" v-model="newListing.price" required>
            </div>
            <div class="mb-3">
              <label for="image" class="form-label">image</label>
              <input type="text" id="image" class="form-control" v-model="newListing.image" required>
            </div>
            <div class="mb-3">
              <label for="category" class="form-label">Category</label>
              <select class="form-select" id="category" v-model="newListing.categoryId" required>
                <option v-for="category in categories" :value="category.id">{{ category.name }}</option>
              </select>
            </div>
            <button type="submit" class="btn btn-primary">Add listing</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>


<script lang="ts" setup>

import type {Category} from "~/models/Category";

const emit = defineEmits(['close', 'add']);

const newListing = {name: "", description: "", price: 0.01, image: "", categoryId: 1};
const props = defineProps<{ categories: Category[] }>();
const categories = props.categories;


const submitForm = () => {
  emit('add', newListing);
};
</script>
