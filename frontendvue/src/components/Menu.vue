<template>
    <div class="container max-w-6xl mx-auto  flex flex-col items-center ">
      <div class="container max-w-6xl bg-gray-800  border border-gray-700 rounded-lg p-6 shadow-lg text-center w-full max-w-lg">
        <h1 class="text-2xl font-bold text-white mb-4">Menu WEKA</h1>
        <img src="@/assets/weka-logo.png" alt="WEKA Logo" class="mx-auto mb-6 w-24">
        <div class="button-list flex flex-col gap-6 w-full">
          <div class="button-item flex flex-col items-center gap-4 text-white">
            <p>
              La edición del archivo CSV permite modificar y actualizar los datos cargados previamente para mejorar la precisión del análisis.
            </p>
            <div class="flex justify-center">
              <button @click="navigate('edit-csv')" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Edit CSV</button>
            </div>
          </div>
          <div class="button-item flex flex-col items-center gap-4 text-white">
            <p>
              La clasificación utilizando árboles de decisión, como J48, construye un modelo predictivo que mapea observaciones sobre un ítem a conclusiones sobre el valor del ítem objetivo.
            </p>
            <div class="flex justify-center">
              <button @click="navigate('classify-decision-trees')" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Classify: Decision Trees</button>
            </div>
          </div>
          <div class="button-item flex flex-col items-center gap-4 text-white">
            <p>
              La clasificación utilizando funciones, como el Perceptrón Multicapa, utiliza una red neuronal para clasificar los datos en función de un conjunto de entradas y salidas esperadas.
            </p>
            <div class="flex justify-center">
              <button @click="navigate('classify-functions')" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Classify: Functions</button>
            </div>
          </div>
          <div class="button-item flex flex-col items-center gap-4 text-white">
            <p>
              El clustering utilizando KMeans agrupa los datos en K clusters distintos, donde cada dato pertenece al cluster con la media más cercana, ayudando a descubrir estructuras ocultas en los datos.
            </p>
            <div class="flex justify-center">
              <button @click="navigate('cluster')" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Cluster</button>

            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  
  <script>
  export default {
    methods: {
      navigate(route) {
        this.$router.push(`/${route}`);
      }
    }
  };
  </script>
  
  <style scoped>
    /* background-color: #f3f4f6; */
    
  /* Puedes agregar estilos adicionales si es necesario */
  </style>
  