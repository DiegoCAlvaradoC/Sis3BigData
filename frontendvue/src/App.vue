<template>
  <div class="container">
    <!-- File Upload Section -->
    <div>
      <input type="file" @change="handleFileUpload">
      <button @click="uploadCsv">Subir CSV</button>
    </div>

    <!-- Decision Tree Generation Section -->
    <div>
      <h2>Arbol J48</h2>
      <form @submit.prevent="generateDecisionTree">
        <div v-if="metadataLoaded">
          <label for="classIndex">Índice de la columna de clase:</label>
          <select id="classIndex" v-model="classIndex">
            <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
          </select>
        </div>
        <br>
        <button type="submit">Generar Árbol de Decisión</button>
      </form>
      <img :src="imagePath" alt="Decision Tree" v-if="imagePath">
    </div>

    <!-- Perceptron Information Generation Section -->
    <div>
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
      <div v-if="classificationResults">
        <h3>Resultados de la clasificación:</h3>
        <pre>{{ classificationResults }}</pre>
      </div>
    </div>

    <!-- Random Forest Generation Section -->
    <!--<div>
      <button @click="generateRandomForestImage">Generar Bosque Aleatorio</button>
      <div v-if="loading" class="loading">Generando bosque aleatorio...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else>
        <img v-if="randomForestImage" :src="randomForestImage" alt="Bosque Aleatorio">
      </div>
    </div>-->

    <!-- Clustering Image Generation Section -->
    <div>
      <h2>Clustering KMeans</h2>
      <form @submit.prevent="generateClusterImage">
        <div v-if="metadataLoaded">
          <label for="attributeX">Atributo X:</label>
          <select id="attributeX" v-model="attributeX">
            <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
          </select>
        </div>
        <br>
        <div v-if="metadataLoaded">
          <label for="attributeY">Atributo Y:</label>
          <select id="attributeY" v-model="attributeY">
            <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
          </select>
        </div>
        <br>
        <label for="numClusters">Número de clusters:</label>
        <input type="number" id="numClusters" v-model="numClusters" required>
        <br>
        <button type="submit">Generar imagen de clustering</button>
      </form>

      <img :src="imagePath" alt="Cluster Image" v-if="imagePath">
    </div>

    <!-- Attribute Selection Section -->
    <div>
      <h2>{{ message }}</h2>
      <div v-if="metadataLoaded">
        <label for="attributeSelect">Selecciona un atributo:</label>
        <select id="attributeSelect" v-model="selectedAttribute">
          <option v-for="(index, attributeName) in metadata" :value="index">{{ attributeName }}</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      file: null,
      loading: false,
      error: '',
      imageUrl: '',
      perceptronInfo: '',
      randomForestImage: '',
      attributeX: 1,
      attributeY: 2,
      numClusters: 3,
      message: '',
      imagePath: '',
      classIndex: 0,
      classificationResults: '',
      metadata: {},
      metadataLoaded: false,
      selectedAttribute: '',
      attributeName: '' // Nuevo dato para almacenar el nombre del atributo
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
            // Aquí puedes manejar la respuesta del servidor
            this.fetchARFFMetadata();
          })
          .catch(error => {
            console.error('Error al subir el archivo CSV:', error);
          });
    },
    generateDecisionTree() {
      fetch(`http://localhost:8080/generate-tree?classIndex=${this.classIndex}`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.blob();
          })
          .then(imageBlob => {
            this.message = 'Árbol de decisión generado correctamente.';
            this.imagePath = URL.createObjectURL(imageBlob);
          })
          .catch(error => {
            console.error('Error:', error);
            this.message = 'Error al generar el árbol de decisión.';
          });
    },
    generatePerceptronInfo() {
      fetch(`http://localhost:8080/generate-perceptron?classIndex=${this.classIndex}`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.text();
          })
          .then(data => {
            this.message = 'Información del perceptrón generada correctamente.';
            this.classificationResults = data;
          })
          .catch(error => {
            console.error('Error:', error);
            this.message = 'Error al generar la información del perceptrón.';
            this.classificationResults = '';
          });
    },
    generateRandomForestImage() {
      this.loading = true;
      fetch('http://localhost:8080/generate-hoeffdingtree', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Error al generar el bosque aleatorio');
            }
            return response.blob();
          })
          .then(blob => {
            this.randomForestImage = URL.createObjectURL(blob);
            this.loading = false;
          })
          .catch(error => {
            this.error = error.message;
            this.loading = false;
          });
    },
    generateClusterImage() {
      fetch(`http://localhost:8080/generate-cluster-image?attributeX=${this.attributeX}&attributeY=${this.attributeY}&numClusters=${this.numClusters}`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.blob();
          })
          .then(imageBlob => {
            this.message = 'Imagen de clustering generada correctamente.';
            this.imagePath = URL.createObjectURL(imageBlob);
          })
          .catch(error => {
            console.error('Error:', error);
            this.message = 'Error al generar el gráfico de clustering.';
          });
    },
    fetchARFFMetadata() {
      fetch(`http://localhost:8080/arff-metadata`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            this.message = 'Metadata obtenida correctamente.';
            this.metadata = data;
            this.metadataLoaded = true;
          })
          .catch(error => {
            console.error('Error:', error);
            this.message = 'Error al obtener la metadata del archivo ARFF.';
            this.metadataLoaded = false;
          });
    }
  },
};
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

h2 {
  margin-top: 0;
}

form {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type="file"] {
  margin-bottom: 10px;
}

select {
  width: 100%;
  padding: 5px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  box-sizing: border-box;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

img {
  max-width: 100%;
  margin-top: 10px;
}

.error {
  color: #ff0000;
  margin-top: 10px;
}

.loading {
  margin-top: 10px;
}
</style>
