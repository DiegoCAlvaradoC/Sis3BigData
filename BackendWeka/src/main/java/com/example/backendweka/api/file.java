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
import java.awt.image.BufferedImage;
import java.io.*;


// Para imagen
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.nio.file.Files;
import java.nio.file.Path;

import java.awt.Color;
import java.io.File;
import java.util.*;
import java.util.List;

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

    public Instances DataLoad;

    // Funcion para cargar el archivo CSV y guardarlo
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
            DataLoad = loader.getDataSet();


            // Agregar la funcionalidad para convertir atributos String a Nominal
            for (int i = 0; i < DataLoad.numAttributes(); i++) {
                if (DataLoad.attribute(i).isString()) {
                    // Convertir el atributo de tipo String a Nominal
                    StringToNominal filter = new StringToNominal();
                    String[] options = {"-R", String.valueOf(i + 1)}; // i+1 porque los índices en Weka comienzan desde 1
                    filter.setOptions(options);
                    filter.setInputFormat(DataLoad);
                    DataLoad = weka.filters.Filter.useFilter(DataLoad, filter);
                }
            }

            // Guardar como ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(DataLoad);
            saver.setFile(new java.io.File("DataCleanWEKA.arff"));
            saver.writeBatch();

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

    @GetMapping(value = "/generate-tree", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateTree(@RequestParam(value = "classIndex") int classIndex) {
        try {
            J48 cls = new J48();
            Instances data = DataLoad;

            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Save tree as DOT file
            String dotFileName = "tree.dot";
            saveTreeAsDot(cls, dotFileName);

            // Convert DOT file to image using Graphviz (assuming Graphviz is installed)
            convertDotToImage(dotFileName, "tree");
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


    @GetMapping(value = "/image-perceptron", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateImgP(@RequestParam(value = "classIndex") int classIndex) {
        try {
            MultilayerPerceptron cls = new MultilayerPerceptron();
            Instances data = DataLoad;

            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Guardar resultados del clasificador en una variable
            StringWriter modelResults = new StringWriter();
            modelResults.append(cls.toString());
            String classificationResults = modelResults.toString();


            // Dividir la entrada en nodos y pesos
            Scanner scanner = new Scanner(classificationResults);
            String[] nodeArray = scanner.nextLine().split(", ");
            List<List<Double>> weights = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.matches(".*\\d.*")) {
                    continue; // Ignorar líneas sin números
                }
                String[] weightArray = line.split(", ");
                List<Double> weightList = new ArrayList<>();
                for (String weight : weightArray) {
                    try {
                        weightList.add(Double.parseDouble(weight));
                    } catch (NumberFormatException e) {
                        // Ignorar cualquier valor no numérico
                    }
                }
                if (!weightList.isEmpty()) {
                    weights.add(weightList);
                }
            }

            // Generar archivo DOT
            try {
                generateDotFile(nodeArray, weights);
                generateGraphImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String imageFilePath = "network.png"; // Cambia esto por la ruta real de tu imagen generada
            File fileImg = new File(imageFilePath);
            Path path = fileImg.toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            // Maneja los errores según sea necesario
            e.printStackTrace();
            return null;
        }
    }


    private static void generateDotFile(String[] nodes, List<List<Double>> weights) throws IOException {
        FileWriter fileWriter = new FileWriter("network.dot");
        fileWriter.write("digraph G {\n");

        // Definir nodos
        for (String node : nodes) {
            fileWriter.write(String.format("\"%s\";\n", node));
        }

        // Definir aristas con pesos
        for (int i = 0; i < nodes.length - 1; i++) {
            for (int j = 0; j < weights.get(i).size(); j++) {
                fileWriter.write(String.format("\"%s\" -> \"%s\" [label=\"%.2f\"];\n", nodes[i], nodes[i + 1], weights.get(i).get(j)));
            }
        }

        fileWriter.write("}\n");
        fileWriter.close();
    }

    private static void generateGraphImage() throws IOException {
        // Comando para generar la imagen con Graphviz
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "network.dot", "-o", "network.png");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Esperar a que el proceso termine
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    // Nueva función para el clasificador MultilayerPerceptron
    @GetMapping(value = "/generate-perceptron", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generatePerceptronInfo(@RequestParam(value = "classIndex") int classIndex) {
        try {
            MultilayerPerceptron cls = new MultilayerPerceptron();
            Instances data = DataLoad;

            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Guardar resultados del clasificador en una variable
            StringWriter modelResults = new StringWriter();
            modelResults.append(cls.toString());
            String classificationResults = modelResults.toString();

            // Extraer información relevante
            //System.out.println("Información: ");
            //System.out.println(classificationResults);
            String relevantInfo = extractRelevantInfo(classificationResults);
            //System.out.println("Información relevante: ");
            //System.out.println(relevantInfo);

            // Guardar la información relevante en un archivo .txt
            String filePath = "perceptron_info.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(classificationResults);
            }

            // Leer el archivo y devolverlo como byte[]
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .body(fileContent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    // Nueva función para el clasificador RandomForest
    private void saveHoeffdingTreeAsDot(HoeffdingTree tree, String fileName) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(tree.graph());
        }
    }

    // Nueva función para el clasificador HoeffdingTree
    @GetMapping(value = "/generate-hoeffdingtree", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateHoeffdingTreeImage() {
        try {
            HoeffdingTree cls = new HoeffdingTree();
            Instances data = DataLoad;

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

    private String extractRelevantInfo(String classificationResults) {
        StringBuilder relevantInfo = new StringBuilder();

        // Aquí puedes agregar los criterios para filtrar la información relevante
        String[] lines = classificationResults.split("\n");
        for (String line : lines) {
            if (line.contains("Correctly Classified Instances") ||
                    line.contains("Incorrectly Classified Instances") ||
                    line.contains("Kappa statistic") ||
                    line.contains("Mean absolute error") ||
                    line.contains("Root mean squared error") ||
                    line.contains("Relative absolute error") ||
                    line.contains("Root relative squared error") ||
                    line.contains("Total Number of Instances")) {
                relevantInfo.append(line).append("\n");
            }
        }

        return relevantInfo.toString();
    }

    @GetMapping("/generate-cluster-image")
    public byte[] generateClusterImage(@RequestParam(value = "attributeX") int attributeX,
                                       @RequestParam(value = "attributeY") int attributeY,
                                       @RequestParam(value = "numClusters", defaultValue = "3") int numClusters) {
        try {
            // Cargar el archivo ARFF
            Instances data = DataLoad;

            // Instanciar SimpleKMeans
            SimpleKMeans kmeans = new SimpleKMeans();
            data.setClassIndex(-1);

            // Configurar el número de clusters (k)
            kmeans.setNumClusters(numClusters);

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

            // Convertir el gráfico en un array de bytes
            BufferedImage image = chart.createBufferedImage(800, 600);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ChartUtils.writeBufferedImageAsPNG(baos, image);

            // Guardar la imagen en un archivo PNG
            String imagePath = "clustering_results.png";
            File outputFile = new File(imagePath);
            ChartUtils.saveChartAsPNG(outputFile, chart, 800, 600);

            // Devolver los bytes de la imagen
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/arff-metadata")
    public Map<String, Integer> getARFFMetadata() {
        Map<String, Integer> metadata = new HashMap<>();
        try {
            // String filename = "dataset_modified.arff"; // Nombre del archivo ARFF
            // DataSource source = new DataSource(filename);
            Instances data = DataLoad; // Cargar el archivo ARFF

            // Obtener nombres de las columnas y sus índices
            for (int i = 0; i < data.numAttributes(); i++) {
                metadata.put(data.attribute(i).name(), i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar errores según sea necesario
        }
        return metadata;
    }

}
