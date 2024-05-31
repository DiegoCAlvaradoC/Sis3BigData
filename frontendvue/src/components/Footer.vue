<template>
    <div class="bg-blue-400 md:px-14 p-4 max-w-screen-2xl mx-auto text-white">
     
      <div class="flex flex-col sm:flex-row gap-8 sm:items-center justify-between my-8">
        <p class="text-center py-4">© {{ currentYear }} Los Puni - Sistemas de información III</p>
        <p class="text-primary font-semibold text-base dark:text-white">Hecho con {"<3"}</p>
        <div class="flex items-center space-x-5">
          <img :src="fb" alt="Facebook" class="w-8 cursor-pointer hover:-translate-y-4 transition-all duration-300" />
          <img :src="ig" alt="Instagram" class="w-8 cursor-pointer hover:-translate-y-4 transition-all duration-300" />
          <img :src="tw" alt="Twitter" class="w-8 cursor-pointer hover:-translate-y-4 transition-all duration-300" />
          <img :src="tt" alt="TikTok" class="w-8 cursor-pointer hover:-translate-y-4 transition-all duration-300" />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import fb from '../assets/fb.png';
  import ig from '../assets/ig.png';
  import tw from '../assets/tw.png';
  import tt from '../assets/tt.png';
  
  export default {
    name: 'Footer',
    data() {
      return {
        fb,
        ig,
        tw,
        tt,
        currentYear: new Date().getFullYear()
      };
    }
  };
  </script>
  
  <style scoped>
  /* Estilos adicionales si es necesario */
  </style>
  