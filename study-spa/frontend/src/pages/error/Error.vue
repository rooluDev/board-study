<template>
  <h1>Error</h1>
  <button @click="goToList">List</button>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  setup() {
    const router = useRouter();
    const goToList = () => {
      router.push({
        name: 'List',
      });
    };
    return {
      goToList,
    };
  },
};
</script>

<style scoped></style>
