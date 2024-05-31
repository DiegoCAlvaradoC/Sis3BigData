<template>
    <div class="container">
        <div class="card">
            <h1>Menu WEKA</h1>
            <img src="@/assets/weka-logo.png" alt="WEKA Logo" class="weka-logo">
            <div class="button-list">

                <div class="button-item">
                    <p>La clasificación utilizando árboles de decisión, como J48, construye un modelo predictivo que mapea observaciones sobre un ítem a conclusiones sobre el valor del ítem objetivo.</p>
                    <button @click="navigate('classify-trees')">Classify: Trees</button>
                </div>
                <div class="button-item">
                    <p>La clasificación utilizando funciones, como el Perceptrón Multicapa, utiliza una red neuronal para clasificar los datos en función de un conjunto de entradas y salidas esperadas.</p>
                    <button @click="navigate('classify-functions')">Classify: Functions</button>
                </div>
                <div class="button-item">
                    <p>El clustering utilizando KMeans agrupa los datos en K clusters distintos, donde cada dato pertenece al cluster con la media más cercana, ayudando a descubrir estructuras ocultas en los datos.</p>
                    <button @click="navigate('cluster')">Cluster</button>
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

.card h1 {
    margin-top: 0;
}

.weka-logo {
    display: block;
    margin: 0 auto 20px;
    width: 100px;
}

.button-list {
    display: flex;
    flex-direction: column;
    text-align: center;
    align-items: center;
    gap: 20px;
    width: 100%;
}

.button-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 10px;
    width: 100%;
}

.button-item p {
    margin: 0;
    font-size: 1rem;
    text-align: center;
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
