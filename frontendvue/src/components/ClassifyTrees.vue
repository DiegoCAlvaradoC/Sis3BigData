<template>
    <div class="container">
      <button @click="$router.push('/menu')">Volver al menú</button>
      <div class="card">
        <h2>Generar Árbol de Decisión J48</h2>
        <form @submit.prevent="generateDecisionTree">
          <div v-if="metadataLoaded" class="form-group">
            <label for="classIndex">Índice de la columna de clase:</label>
            <select id="classIndex" v-model="classIndex">
              <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
            </select>
          </div>
          <br>
          <button type="submit">Generar Árbol de Decisión</button>
        </form>
        <img :src="imagePath2" alt="Decision Tree" v-if="imagePath2">
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        classIndex: 0,
        imagePath2: '',
        metadata: {},
        metadataLoaded: false
      };
    },
    methods: {
      generateDecisionTree() {
        axios.get(`http://localhost:8080/generate-tree?classIndex=${this.classIndex}`, { responseType: 'blob' })
          .then(response => {
            this.imagePath2 = URL.createObjectURL(response.data);
          })
          .catch(error => {
            console.error('Error al generar el árbol de decisión:', error);
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
  