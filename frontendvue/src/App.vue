<template>
  <div id="app">
    <router-view></router-view>
    <Footer />
  </div>
</template>

<script>
import Footer from './components/Footer.vue';

export default {
  name: 'App',
  components: {
    Footer,
    // Importa tus componentes aquí
  },
}
</script>

<style>
/* Tus estilos aquí */
</style>
