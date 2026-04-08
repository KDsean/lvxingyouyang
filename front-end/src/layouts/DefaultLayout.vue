<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <router-view />
    <Footer />
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/common/NavBar.vue'
import Footer from '@/components/common/Footer.vue'
</script>
