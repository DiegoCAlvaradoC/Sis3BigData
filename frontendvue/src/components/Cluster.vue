<template>
  <div class="container">
    <button @click="$router.push('/menu')" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
      Volver al menú
    </button>
    <div class="card bg-white rounded-lg shadow-lg p-6 mt-4">
      <h2 class="text-xl font-bold mb-4">Generar Clustering KMeans</h2>
      <form @submit.prevent="generateClusterImage">
        <div v-if="metadataLoaded" class="form-group">
          <label for="attributeX" class="text-gray-700">Atributo X:</label>
          <select id="attributeX" v-model="attributeX" class="form-select mt-1 block w-full">
            <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
          </select>
        </div>
        <div v-if="metadataLoaded" class="form-group">
          <label for="attributeY" class="text-gray-700">Atributo Y:</label>
          <select id="attributeY" v-model="attributeY" class="form-select mt-1 block w-full">
            <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
          </select>
        </div>
        <div class="form-group">
          <label for="numClusters" class="text-gray-700">Número de Clusters:</label>
          <input type="number" id="numClusters" v-model="numClusters" required class="form-input mt-1 block w-full rounded-md">
        </div>
        <br/>
        <div class="flex justify-center">
          <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Generar Imagen de Clustering</button>
        </div>
        
      </form>
        <img :src="imagePath1" alt="Cluster Image" v-if="imagePath1">
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        attributeX: 1,
        attributeY: 2,
        numClusters: 3,
        imagePath1: '',
        metadata: {},
        metadataLoaded: false
      };
    },
    methods: {
      generateClusterImage() {
        axios.get(`http://localhost:8080/generate-cluster-image?attributeX=${this.attributeX}&attributeY=${this.attributeY}&numClusters=${this.numClusters}`, { responseType: 'blob' })
          .then(response => {
            this.imagePath1 = URL.createObjectURL(response.data);
          })
          .catch(error => {
            console.error('Error al generar el gráfico de clustering:', error);
          });
      },
      fetchARFFMetadata() {
        axios.get('http://localhost:8080/arff-metadata')
          .then(response => {
            this.metadata = response.data;
            this.metadataLoaded = true;
          })
          .catch(error => {
            console.error('Error al obtener la metadata del archivo ARFF:', error);
          });
      }
    },
    created() {
      this.fetchARFFMetadata();
    }
  };
  </script>
  
  <style>
  body {
    font-family: 'Metropolis', sans-serif;
    background-color: #1f1f1f;
    color: #fff;
    margin: 0;
    padding: 0;
  }
  
  .link {
    color: #b3d6fb;
  
  }
  
  .container {
    max-width: 1200px;
    margin: 20px auto;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .card {
    background-color: #2c2c2c;
    border: 1px solid #444;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  
  .card h2 {
    margin-top: 0;
  }
  
  .file-upload {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  label {
    font-weight: bold;
  }
  
  input[type="file"],
  select,
  input[type="number"] {
    width: 100%;
    padding: 10px;
    border: 1px solid #444;
    border-radius: 4px;
    background-color: #333;
    color: #fff;
  }
  
  button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    align-self: flex-start;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  
  img {
    max-width: 100%;
    margin-top: 20px;
    border-radius: 8px;
  }
  
  pre {
    background-color: #333;
    padding: 10px;
    border-radius: 4px;
    overflow-x: auto;
  }
  
  .error {
    color: #ff0000;
    margin-top: 10px;
  }
  
  .loading {
    margin-top: 10px;
  }
  </style>
  