<template>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-6xl mx-auto bg-white shadow-lg rounded-lg overflow-hidden">
        <header class="bg-blue-400 text-white text-center py-4">
          <h2 class="text-2xl font-bold">
            Diseñar un Sistema de Análisis de Datos para Evaluar el Impacto del Consumo de Alcohol en el Rendimiento Académico
          </h2>
        </header>
        <!-- Añadimos el Banner aquí -->
        <Banner 
          banner="ruta/a/la/imagen-del-banner.jpg"
          heading="Sistema de Análisis de Datos"
          subheading="Evaluando el impacto del consumo de alcohol en el rendimiento académico de los estudiantes"
        />
        <div class="p-6 text-blue-500">
          <h2 class="text-xl font-semibold mb-4">Proyecto de Datasets y Machine Learning</h2>
          <p class="mb-4">
          Este proyecto tiene como objetivo diseñar un sistema de análisis de datos especializado en evaluar el impacto del consumo de alcohol en el rendimiento académico de los estudiantes. Utilizaremos Weka para aplicar técnicas de machine learning como árboles de decisión, clústeres y perceptrones. Esto proporcionará a las instituciones educativas herramientas avanzadas para apoyar en la toma de decisiones.
          </p>
        <!-- button to charge the csv-->
        <input class="custom-file-input" type="file" @change="handleFileUpload" accept=".csv" />
        <div class="flex justify-center">
            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="uploadCsv">Subir CSV</button>
        </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Banner from './Banner.vue';
  import axios from 'axios';
  
  export default {
    name: 'Home',
    components: {
      Banner,
    },
    data() {
      return {
        file: null,
        message: ''
      };
    },
    methods: {
      handleFileUpload(event) {
        this.file = event.target.files[0];
      },
      uploadCsv() {
        let formData = new FormData();
        formData.append('file', this.file);
  
        axios.post('http://localhost:8080/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          console.log(response.data);
          this.message = 'Archivo subido correctamente. Redirigiendo...';
          setTimeout(() => {
            this.$router.push('/menu');
          }, 1000);
        })
        .catch(error => {
          console.error('Error al subir el archivo CSV:', error);
          this.message = 'Error al subir el archivo.';
        });
      }
    }
  };
  </script>
  <style scoped>
  .custom-file-input {
    background-color: #2196F3;
    color: #fff; /* text-white */
    font-weight: bold; /* font-bold */
    padding: 0.5rem 1rem; /* py-2 px-4 */
    border-radius: 0.25rem; /* rounded */
  }
  
  .custom-file-input:hover {
    background-color: #3182ce; /* hover:bg-blue-500 */
  }
  </style>