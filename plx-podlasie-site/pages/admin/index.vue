<script setup lang="ts">
const loading = ref(true);
onMounted(() => {
  loading.value = false;
})
</script>

<template>
  <div v-if="!loading">
    <NavbarAdmin />

  </div>
</template>

<style scoped></style>
