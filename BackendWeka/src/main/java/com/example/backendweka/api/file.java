package com.example.backendweka.api;

// Generales

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Carga CSV

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.unsupervised.attribute.StringToNominal;


import java.awt.*;
import java.io.File;


// Para imagen
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@SpringBootApplication
@RestController
public class file {

    public Instances DataLoad = null;

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
    @GetMapping(value = "/generate-tree", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateTree() {
        try {
            J48 cls = new J48();
            String file = "dataset_modified.arff";
            Instances data = DataSource.read(file);

            int classIndex = 0; // Cambia este valor al índice de la columna que deseas establecer como clase
            data.setClassIndex(classIndex);

            cls.buildClassifier(data);

            // Save tree as DOT file
            String dotFileName = "tree.dot";
            saveTreeAsDot(cls, dotFileName);

            // Convert DOT file to image using Graphviz (assuming Graphviz is installed)
            convertDotToImage(dotFileName);
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

    private void convertDotToImage(String dotFileName) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("dot -Tpng " + dotFileName + " -o tree.png");
        p.waitFor();
    }
}
