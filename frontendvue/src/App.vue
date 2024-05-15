<template>
  <div>
    <input type="file" @change="handleFileUpload">
    <button @click="uploadCsv">Subir CSV</button>
  </div>
  <div>
    <form @submit.prevent="generateDecisionTree">
      <label for="classIndex">Índice de la columna de clase:</label>
      <input type="number" id="classIndex" v-model="classIndex" required>
      <br>
      <button type="submit">Generar Árbol de Decisión</button>
    </form>
    <h2>{{ message }}</h2>
    <img :src="imagePath" alt="Decision Tree" v-if="imagePath">
  </div>
  <div>
    <form @submit.prevent="generatePerceptronInfo">
      <label for="classIndex">Índice de la columna de clase:</label>
      <input type="number" id="classIndex" v-model="classIndex" required>
      <br>
      <button type="submit">Generar Información del Perceptrón</button>
    </form>
    <h2>{{ message }}</h2>
    <div v-if="classificationResults">
      <h3>Resultados de la clasificación:</h3>
      <pre>{{ classificationResults }}</pre>
    </div>
  </div>
  <div>
    <button @click="generateRandomForestImage">Generar Bosque Aleatorio</button>
    <div v-if="loading">Generando bosque aleatorio...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <img v-if="randomForestImage" :src="randomForestImage" alt="Bosque Aleatorio">
    </div>
  </div>
  <div>
    <form @submit.prevent="generateClusterImage">
      <label for="attributeX">Atributo X:</label>
      <input type="number" id="attributeX" v-model="attributeX" required>
      <br>
      <label for="attributeY">Atributo Y:</label>
      <input type="number" id="attributeY" v-model="attributeY" required>
      <br>
      <label for="numClusters">Número de clusters:</label>
      <input type="number" id="numClusters" v-model="numClusters" required>
      <br>
      <button type="submit">Generar imagen de clustering</button>
    </form>
    <h2>{{ message }}</h2>
    <img :src="imagePath" alt="Cluster Image" v-if="imagePath">
  </div>
  <div>
    <button @click="fetchARFFMetadata">Obtener Metadata del ARFF</button>
    <h2>{{ message }}</h2>
    <div v-if="metadata">
      <h3>Metadata del archivo ARFF:</h3>
      <pre>{{ metadata }}</pre>
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
      metadata: null,
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
            this.metadata = JSON.stringify(data, null, 2);
          })
          .catch(error => {
            console.error('Error:', error);
            this.message = 'Error al obtener la metadata del archivo ARFF.';
            this.metadata = null;
          });
    }
  },
};
</script>


<style>
#app {
  font-family: Arial, sans-serif;
  text-align: center;
  margin-top: 50px;
}

</style>




