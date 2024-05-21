package com.example.backendweka.api;

// Generales

import org.jfree.chart.axis.NumberAxis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Carga CSV

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.unsupervised.attribute.StringToNominal;


import java.awt.*;
import java.io.*;


// Para imagen
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.nio.file.Files;
import java.nio.file.Path;

import java.awt.Color;
import java.io.File;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@SpringBootApplication
@RestController
public class file {

    public Instances DataLoad = null;

    // Funcion para cargar el archivo CSV y guardarlo
    @CrossOrigin(origins = "http://10.0.2.2:8080") // Add the origin you want to allow
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestBody MultipartFile file) {
        // Verificar si el archivo es nulo o está vacío
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha proporcionado ningún archivo.");
        }

        // Imprimir información sobre el archivo recibido en la consola
        System.out.println("Nombre del archivo: " + file.getOriginalFilename());
        System.out.println("Tipo de contenido: " + file.getContentType());
        System.out.println("Tamaño del archivo: " + file.getSize() + " bytes");

        try {
            // Cargar archivo CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(file.getInputStream());
            Instances data = loader.getDataSet();

            // Agregar la funcionalidad para convertir atributos String a Nominal
            for (int i = 0; i < data.numAttributes(); i++) {
                if (data.attribute(i).isString()) {
                    // Convertir el atributo de tipo String a Nominal
                    StringToNominal filter = new StringToNominal();
                    String[] options = {"-R", String.valueOf(i + 1)}; // i+1 porque los índices en Weka comienzan desde 1
                    filter.setOptions(options);
                    filter.setInputFormat(data);
                    data = weka.filters.Filter.useFilter(data, filter);
                }
            }

            // Guardar como ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new java.io.File("DataCleanWEKA.arff"));
            saver.writeBatch();

            DataLoad = data;

            System.out.println("Archivo ARFF creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al procesar el archivo.");
        }

        // Aquí puedes procesar el archivo CSV recibido
        // Por ejemplo, puedes guardar el archivo en el sistema de archivos,
        // analizarlo, manipular los datos, etc.
        // En este ejemplo, simplemente devolvemos un mensaje indicando que el archivo se recibió correctamente.
        return ResponseEntity.status(HttpStatus.OK).body("El archivo CSV fue recibido correctamente.");
    }

    @CrossOrigin(origins = "http://10.0.2.2:8080")
    @GetMapping(value = "/generate-tree", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateTree() {
        try {
            J48 cls = new J48();
            String file = "dataset_modified.arff";

            Instances data = null;
            try{
                data = DataSource.read(file);
                System.out.println("File successfuly loaded");
            }
            catch (Exception e){
                System.out.println("No file found");
                e.printStackTrace();
            }


            int classIndex = 0; // Cambia este valor al índice de la columna que deseas establecer como clase
            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Save tree as DOT file
            String dotFileName = "tree.dot";
            saveTreeAsDot(cls, dotFileName);

            // Convert DOT file to image using Graphviz (assuming Graphviz is installed)
            convertDotToImage(dotFileName,"tree");
            String imageFilePath = "tree.png"; // Cambia esto por la ruta real de tu imagen generada
            File fileImg = new File(imageFilePath);
            Path path = fileImg.toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            // Maneja los errores según sea necesario
            e.printStackTrace();
            return null;
        }
    }

    private void saveTreeAsDot(J48 cls, String fileName) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(cls.graph());
        }
    }

    private void convertDotToImage(String dotFileName, String nameImg) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("dot -Tpng " + dotFileName + " -o "+nameImg+".png");
        System.out.println("Imagen generada con éxito: "+ nameImg + ".png");
        p.waitFor();
    }

    // Nueva función para el clasificador MultilayerPerceptron
    @CrossOrigin(origins = "http://10.0.2.2:8080")
    @GetMapping(value = "/generate-perceptron")
    public ResponseEntity<String> generatePerceptronInfo() {
        try {
            MultilayerPerceptron cls = new MultilayerPerceptron();
            String file = "dataset_modified.arff";
            Instances data = DataSource.read(file);

            int classIndex = 0; // Cambia este valor al índice de la columna que deseas establecer como clase
            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Guardar resultados del clasificador en una variable
            StringWriter modelResults = new StringWriter();
            modelResults.append(cls.toString());
            String classificationResults = modelResults.toString();
            System.out.println(classificationResults);

            return ResponseEntity.status(HttpStatus.OK).body(classificationResults);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al generar la información del clasificador.");
        }
    }

    // Nueva función para el clasificador RandomForest
    private void saveHoeffdingTreeAsDot(HoeffdingTree tree, String fileName) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(tree.graph());
        }
    }

    // Nueva función para el clasificador HoeffdingTree
    @CrossOrigin(origins = "http://10.0.2.2:8080")
    @GetMapping(value = "/generate-hoeffdingtree", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateHoeffdingTreeImage() {
        try {
            HoeffdingTree cls = new HoeffdingTree();
            String file = "dataset_modified.arff";
            Instances data = DataSource.read(file);

            int classIndex = 0; // Cambia este valor al índice de la columna que deseas establecer como clase
            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Guardar el HoeffdingTree como archivo DOT
            String dotFileName = "hoeffding_tree.dot";
            saveHoeffdingTreeAsDot(cls, dotFileName);

            // Convertir archivo DOT a imagen usando Graphviz
            convertDotToImage(dotFileName,"hoeffding_tree");
            String imageFilePath = "hoeffding_tree.png"; // Cambia esto por la ruta real de tu imagen generada
            File fileImg = new File(imageFilePath);
            Path path = fileImg.toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @CrossOrigin(origins = "http://10.0.2.2:8080")
    @GetMapping("/generate-cluster-image")
    public String clusterData() {
        try {
            // Cargar el archivo ARFF
            DataSource source = new DataSource("dataset_modified.arff");
            Instances data = source.getDataSet();

            // Seleccionar los índices de los atributos que deseas utilizar
            int attributeX = 1; // Índice del atributo X
            int attributeY = 2; // Índice del atributo Y

            // Instanciar SimpleKMeans
            SimpleKMeans kmeans = new SimpleKMeans();

            // Configurar el número de clusters (k)
            kmeans.setNumClusters(3); // Por ejemplo, 3 clusters

            // Asegurar que el orden de las instancias se mantenga
            kmeans.setPreserveInstancesOrder(true);

            // Construir el modelo
            kmeans.buildClusterer(data);

            // Obtener los resultados del clustering
            int[] assignments = kmeans.getAssignments();

            // Crear un conjunto de datos para el gráfico de dispersión
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries[] series = new XYSeries[kmeans.getNumClusters()];

            for (int i = 0; i < kmeans.getNumClusters(); i++) {
                series[i] = new XYSeries("Cluster " + (i + 1));
                dataset.addSeries(series[i]);
            }

            // Agregar las instancias al conjunto de datos según el cluster al que pertenecen
            for (int i = 0; i < data.numInstances(); i++) {
                Instance instance = data.instance(i);
                int cluster = assignments[i];
                series[cluster].add(instance.value(attributeX), instance.value(attributeY));
            }

            // Crear el gráfico de dispersión
            JFreeChart chart = ChartFactory.createScatterPlot(
                    "Clustering Results",
                    "Atributo " + attributeX,
                    "Atributo " + attributeY,
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            // Guardar el gráfico como un archivo PNG
            String chartFilePath = "clustering_results.png";
            File chartFile = new File(chartFilePath);
            ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600);

            return "Gráfico de clustering generado correctamente. La imagen ha sido guardada en " + chartFilePath;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar los datos para clustering.";
        }
    }

}
