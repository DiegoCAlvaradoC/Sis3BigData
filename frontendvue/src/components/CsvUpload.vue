<template>
  <div class="container">
    <div class="card">
      <h2>Subir Archivo CSV</h2>
      <div class="file-upload">
        <input type="file" @change="handleFileUpload">
        <button @click="parseCsv">Cargar CSV</button>
      </div>
      <div v-if="message" class="message">{{ message }}</div>
      <div v-if="csvData.length" class="csv-table-wrapper">
        <div class="csv-table-container">
          <table class="csv-table">
            <thead>
            <tr>
              <th v-for="(header, index) in csvData[0]" :key="index">{{ header }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(row, rowIndex) in displayedRows" :key="rowIndex">
              <td v-for="(cell, cellIndex) in row" :key="cellIndex">
                <input v-model="csvData[rowIndex][cellIndex]" />
              </td>
            </tr>
            </tbody>
          </table>
        </div>

      </div>
      <button @click="saveCsv">Guardar CSV</button>
    </div>
  </div>
</template>

<script>
import { parse, unparse } from 'papaparse';
import axios from 'axios';

export default {
  data() {
    return {
      file: null,
      message: '',
      csvData: [],
      displayedRows: [] // Limitar el número de filas mostradas
    };
  },
  methods: {
    handleFileUpload(event) {
      this.file = event.target.files[0];
    },
    parseCsv() {
      if (this.file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          const csv = e.target.result;
          const result = parse(csv, { header: false });
          this.csvData = result.data;
          this.displayedRows = this.csvData; // Mostrar todas las filas del CSV
        };
        reader.readAsText(this.file);
      }
    },
    saveCsv() {
      const csv = unparse(this.csvData);
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });

      // Guardar archivo localmente
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'edited_file.csv';
      link.click();

      // Enviar archivo al backend
      const formData = new FormData();
      formData.append('file', blob, 'edited_file.csv');
      axios.post('http://localhost:8080/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
          .then(response => {
            console.log(response.data);
            this.message = 'Archivo guardado y enviado al backend correctamente.';
            setTimeout(() => {
              this.$router.push('/menu');
            }, 1000);
          })
          .catch(error => {
            console.error('Error al guardar el archivo CSV:', error);
            this.message = 'Error al guardar el archivo csv.';
          });
    }
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
  overflow-x: auto;
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

.csv-table-wrapper {
  overflow-y: auto; /* Agregar scroll vertical */
  max-height: 650px; /* Limitar la altura para mostrar solo un número determinado de filas */
}

.csv-table-container {
  overflow-x: auto;
}

.csv-table {
  border-collapse: collapse;
  margin-top: 10px;
  min-width: 100%;
}

.csv-table th,
.csv-table td {
  border: 1px solid #444;
  padding: 8px;
  text-align: left;
}

.csv-table th {
  background-color: #333;
}

.csv-table input {
  width: 100%;
  padding: 4px;
  border: none;
  background-color: #444;
  color: #fff;
  box-sizing: border-box;
}
</style>
