<template>
  <div v-if="listing" class="modal fade show" tabindex="-1" style="display: block;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Listing</h5>
          <button type="button" class="btn-close" aria-label="Close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Name</label>
              <input type="text" id="name" class="form-control" v-model="localListing.name" required>
            </div>
            <div class="mb-3">
              <label for="description" class="form-label">Description</label>
              <textarea id="description" class="form-control" v-model="localListing.description"
                        required></textarea>
            </div>
            <div class="mb-3">
              <label for="image" class="form-label">Image</label>
              <textarea id="image" class="form-control" v-model="localListing.image"
                        required></textarea>
            </div>
            <div class="mb-3">
              <label for="price" class="form-label">Price</label>
              <input type="number" step="0.01" id="price" class="form-control" v-model="localListing.price" required>
            </div>
            <button type="submit" class="btn btn-primary">Save changes</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type {Listing} from "~/models/Listing";

const props = defineProps<{ listing: Listing }>();
const emit = defineEmits(['close', 'update']);

const localListing = ref(props.listing);


const submitForm = () => {
  if (localListing.value) {
    emit('update', localListing.value);
  }
};
</script>
