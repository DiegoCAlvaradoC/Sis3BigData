<template>
    <div>
        <div class="bg-blue-400 rounded-xl rounded-br-[80px] md:p-9 px-4 py-9">
      <div class="flex flex-col md:flex-row-reverse justify-between items-center gap-10">
          <motion-div
            :initial="{ opacity: 0, y: -50 }"
            :animate="{ opacity: 1, y: 0 }"
            transition="{ delay: 0.2 }"
          >
            <img :src="Fondo" alt="Descripción de la imagen" class="lg:h-[300px]" />
          </motion-div>
          <motion-div
            :initial="{ opacity: 0, y: 50 }"
            :animate="{ opacity: 1, y: 0 }"
            transition="{ delay: 0.2 }"
            class="md:w-3/5"
          >
            <h2 class="md:text-7xl text-4xl font-bold text-white mb-6 leading-relaxed">{{ heading }}</h2>
            <p class="text-[#EBEBEB] text-2xl mb-8">{{ subheading }}</p>
            <div class="space-x-5 space-y-4">
              <button v-if="bt1 && bt1 !== ''" class="btnPrimary">{{ bt1 }}</button>
              <button v-if="bt2 && bt2 !== ''" class="btnPrimary">{{ bt2 }}</button>
            </div>
          </motion-div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { useMotion } from '@vueuse/motion'
  import Fondo from '../assets/fondo1.jpg'
  export default {
    name: 'Banner',
    props: {
      banner: String,
      heading: String,
      subheading: String,
      bt1: String,
      bt2: String
    },
    data() {
      return {
        Fondo
      }
    },
    setup() {
      useMotion()
    }
  }
  </script>
  
  <style scoped>
  /* Estilos adicionales si es necesario */
  </style>