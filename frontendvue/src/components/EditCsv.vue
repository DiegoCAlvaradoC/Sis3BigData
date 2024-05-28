<template>
    <div class="container">
      <div class="card">
        <h2>Editar Archivo CSV</h2>
        <div class="form-group">
          <label for="selectedRow">Seleccionar Fila:</label>
          <select id="selectedRow" v-model="selectedRow">
            <option v-for="(row, index) in data" :value="index" :key="index">
              Fila {{ index + 1 }}
            </option>
          </select>
        </div>
  
        <div v-if="selectedRow !== null" class="form-group">
          <label v-for="(value, key) in data[selectedRow]" :key="key">
            {{ key }}:
            <input type="text" v-model="data[selectedRow][key]" />
          </label>
        </div>
  
        <button @click="saveChanges">Guardar Cambios</button>
        <button @click="$router.push('/menu')">Volver al Menú</button>
  
        <div v-if="message" class="message">{{ message }}</div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        data: [],
        selectedRow: null,
        message: ''
      };
    },
    methods: {
      fetchData() {
        axios.get('http://localhost:8080/arff-metadata')
          .then(response => {
            const metadata = response.data;
            this.data = Object.keys(metadata).map(key => {
              return { attribute: key, index: metadata[key] };
            });
          })
          .catch(error => {
            console.error('Error al obtener la metadata del archivo ARFF:', error);
          });
      },
      saveChanges() {
        axios.post('http://localhost:8080/update-csv', this.data)
          .then(response => {
            this.message = 'Cambios guardados correctamente.';
          })
          .catch(error => {
            console.error('Error al guardar los cambios:', error);
            this.message = 'Error al guardar los cambios.';
          });
      }
    },
    created() {
      this.fetchData();
    }
  };
  </script>
  
  <style>
  /*body {
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
    max-width: 800px;
    margin: 20px auto;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }
  
  .card {
    background-color: #2c2c2c;
    border: 1px solid #444;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    text-align: center;
    width: 100%;
  }
  
  .card h2 {
    margin-top: 0;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
  
  label {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  
  input[type="text"],
  select {
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
  }
  
  button:hover {
    background-color: #0056b3;
  }
  
  .message {
    margin-top: 20px;
    color: #00ff00;
  }
  
  .error {
    color: #ff0000;
    margin-top: 10px;
  }
  
  .loading {
    margin-top: 10px;
  }*/
  </style>
  