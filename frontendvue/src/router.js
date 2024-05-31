import { createRouter, createWebHistory } from 'vue-router';
import CsvUpload from './components/CsvUpload.vue';
import Menu from './components/Menu.vue';
import ClassifyTrees from './components/ClassifyTrees.vue';
import ClassifyFunctions from './components/ClassifyFunctions.vue';
import Cluster from './components/Cluster.vue';
import EditCsv from './components/EditCsv.vue'; // Asegúrate de tener este componente creado

const routes = [
  { path: '/', component: CsvUpload },
  { path: '/menu', component: Menu },
  { path: '/classify-trees', component: ClassifyTrees },
  { path: '/classify-functions', component: ClassifyFunctions },
  { path: '/cluster', component: Cluster },
  { path: '/edit-csv', component: EditCsv }, // Nueva ruta para la edición del CSV
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
