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
          <h2>Resultados de la Evaluación</h2>
          <pre class="text-evaluation">{{ evaluationResults }}</pre>
          <div v-if="error2">
            <p>Error: {{ error2 }}</p>
          </div>
          <img :src="imageP" alt="Image Perceptron" />

        </div>

      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import image1 from '../assets/P_AprobacionPadres.png';
  import image2 from '../assets/P_Asigancion.png';
  import image3 from '../assets/P_Bebidas.png';
  import image4 from '../assets/P_Beca.png';
  import image5 from '../assets/P_classPerd.png';
  import image6 from '../assets/P_Facultad.png';
  import image7 from '../assets/P_Fiestas.png';
  import image8 from '../assets/P_HorasEst.png';
  import image9 from '../assets/P_ModSuspend.png';
  import image10 from '../assets/P_prom12.png';
  import image11 from '../assets/P_Prom23.png';
  import image12 from '../assets/P_Relacion.png';
  import image13 from '../assets/P_RelacionPadres.png';
  import image14 from '../assets/P_sexo.png';
  import image15 from '../assets/P_ultim.png';
  export default {
    data() {
      return {
        classIndex: 0,
        imageP: 0,
        perceptronFilePath: '',
        metadata: {},
        metadataLoaded: false,
        evaluationResults: '',
        error2: '',
        imageNumber: 1, // Inicialmente el número puede ser 1
        imagePaths: [
          image1,
          image2,
          image3,
          image4,
          image5,
          image6,
          image7,
          image8,
          image9,
          image10,
          image11,
          image12,
          image13,
          image14,
          image15,

        ],
      };
    },
    methods: {
      getImageSrc(number) {
        // Ajusta el índice del número para asegurarte de que esté dentro del rango
        const index = number - 1;
        if (index >= 0 && index < this.imagePaths.length) {
          return this.imagePaths[index];
        }
        return 'path/to/default_image.jpg'; // Imagen por defecto si el número no es válido
      },
      generatePerceptronInfo() {
        axios.get(`http://localhost:8080/generate-perceptron?classIndex=${this.classIndex}`, { responseType: 'blob' })
          .then(async response => {
            const url = URL.createObjectURL(response.data);

            // Obtener Evaluacion de clasificacion
            this.evaluationResults = '';
            this.error2 = '';
            try {
              const response = await fetch(`http://localhost:8080/evaluate-perceptron?classIndex=${this.classIndex}`);
              if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
              const result = await response.text();
              this.evaluationResults = result;
              this.imageP = this.getImageSrc(this.classIndex+1);
              this.perceptronFilePath = url;

            } catch (e) {
              this.error = e.message;
            }


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

  .text-evaluation {
    text-align: left;
    white-space: pre-wrap; /* Para preservar los saltos de línea */
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
  