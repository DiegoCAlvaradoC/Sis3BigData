<template>
  <div class="min-h-screen bg-gradient-to-r from-gray-800 via-gray-600 to-gray-400 p-4">
    <div class="max-w-6xl mx-auto bg-transparent shadow-lg rounded-lg overflow-hidden">
      <header class="bg-gradient-to-r from-blue-500 to-purple-600 text-white text-center py-6">
        <h2 class="text-3xl font-bold">
          Sistema de Análisis de Datos para Evaluar el Impacto del Consumo de Alcohol en el Rendimiento Académico
        </h2>
      </header>
      <div class="my-6"></div>
      <!-- Añadimos el Banner aquí -->
      <Banner 
        banner="/src/assets/imagen.jpg"
        heading="Sistema de Análisis de Datos"
        subheading="Evaluando el impacto del consumo de alcohol en el rendimiento académico de los estudiantes"
      />
      <div class="my-6"></div>

      <div class="bg-white bg-opacity-80 rounded-lg p-6 text-blue-500 shadow-md">
        <h2 class="text-2xl font-semibold mb-4">Proyecto de Datasets y Machine Learning</h2>
        <p class="mb-4">
          Este proyecto tiene como objetivo diseñar un sistema de análisis de datos especializado en evaluar el impacto del consumo de alcohol en el rendimiento académico de los estudiantes. Utilizaremos Weka para aplicar técnicas de machine learning como árboles de decisión, clústeres y perceptrones. Esto proporcionará a las instituciones educativas herramientas avanzadas para apoyar en la toma de decisiones.
        </p>
        <!-- button to charge the csv-->
        <input class="custom-file-input mb-4" type="file" @change="handleFileUpload" accept=".csv" />
        <div class="flex justify-center">
          <button class="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-700 hover:to-purple-800 text-white font-bold py-2 px-4 rounded transition-transform transform hover:scale-105" @click="uploadCsv">Subir CSV</button>
        </div>
        <p class="text-center text-red-500 mt-4">{{ message }}</p>
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
      if (!this.file) {
        this.message = 'Por favor, seleccione un archivo.';
        return;
      }
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
  transition: background-color 0.3s;
}

.custom-file-input:hover {
  background-color: #3182ce; /* hover:bg-blue-500 */
}

.bg-gradient-to-r {
  background: linear-gradient(to right, var(--tw-gradient-stops));
}
</style>
