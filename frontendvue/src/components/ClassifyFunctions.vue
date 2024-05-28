<template>
    <div class="container">
      <button @click="$router.push('/menu')">Volver al menú</button>
      <div class="card">
        <h2>Multilayer Perceptron</h2>
        <form @submit.prevent="generatePerceptronInfo">
          <div v-if="metadataLoaded">
            <label for="classIndex">Índice de la columna de clase:</label>
            <select id="classIndex" v-model="classIndex">
              <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
            </select>
          </div>
          <br>
          <button type="submit">Generar Información del Perceptrón</button>
        </form>
        <div v-if="perceptronFilePath">
          <h3>Información del Perceptrón generada:</h3>
          <a :href="perceptronFilePath" download="perceptron_info.txt" class="link">Descargar archivo de información del perceptrón</a>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        classIndex: 0,
        perceptronFilePath: '',
        metadata: {},
        metadataLoaded: false
      };
    },
    methods: {
      generatePerceptronInfo() {
        axios.get(`http://localhost:8080/generate-perceptron?classIndex=${this.classIndex}`, { responseType: 'blob' })
          .then(response => {
            const url = URL.createObjectURL(response.data);
            this.perceptronFilePath = url;
          })
          .catch(error => {
            console.error('Error al generar la información del perceptrón:', error);
            this.perceptronFilePath = '';
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
  