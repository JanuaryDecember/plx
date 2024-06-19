<template>
  <div v-if="category" class="modal fade show" tabindex="-1" style="display: block;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit category</h5>
          <button type="button" class="btn-close" aria-label="Close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Name</label>
              <input type="text" id="name" class="form-control" v-model="localCategory.name" required>
            </div>
            <div class="mb-3">
              <label for="image" class="form-label">Image</label>
              <textarea id="image" class="form-control" v-model="localCategory.image" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Save changes</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { Category } from '~/models/Category';


const props = defineProps<{ category: Category }>();
const emit = defineEmits(['close', 'update']);

const localCategory = ref(props.category);


const submitForm = () => {
  if (localCategory.value) {
    emit('update', localCategory.value);
  }
};
</script>
