package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class subirLibroController {

    BaseDatosOracle conexionOracle;

    BaseDatosMongo conexionMongo;

    @FXML
    private TextField autor;

    @FXML
    private TextField cantidad;

    @FXML
    private ComboBox<String> categoria2;

    @FXML
    private ComboBox<String> categoria3;

    @FXML
    private TextField categoriaN;

    @FXML
    private Pane contenedor;

    @FXML
    private TextField editorial;

    @FXML
    private TextField id;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nombre;

    @FXML
    private TextArea sinopsis;

    private Usuario usuarioActual;

    private File selectedImageFile;
    private static final String portadas_url = "src/main/resources/images/libros/";

    public void setUsuario(Usuario usuario) {
            this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();

        conexionMongo = new BaseDatosMongo();

        cargarCategorias();
    }

    @FXML
    void handleAgregarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void cargarCategorias(){
        ObservableList<Categoria> categorias = conexionOracle.obtenerCategorias();

        categoria2.getItems().add("Ninguna");
        categoria3.getItems().add("Ninguna");

        for (Categoria categoria : categorias) {
            categoria2.getItems().add(categoria.getNombre());
            categoria3.getItems().add(categoria.getNombre());
        }

        categoria2.setValue("Ninguna");
        categoria3.setValue("Ninguna");
    }

    private String guardarImagen(int libroId) {
        if (selectedImageFile == null) {
            return null;
        }

        try {
            File directory = new File(portadas_url);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
            
            String nombreArchivo = "libro_" + libroId + extension;
            File destFile = new File(directory, nombreArchivo);

            Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String rutaFinal = "images/libros/" + nombreArchivo;

            return rutaFinal;
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
            return null;
        }
    }

    @FXML
    void handleAgregarLibro(ActionEvent event) {
        try {
            if (id.getText().isEmpty() || nombre.getText().isEmpty() || autor.getText().isEmpty() || 
                editorial.getText().isEmpty() || cantidad.getText().isEmpty() || sinopsis.getText().isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", AlertType.ERROR);
                return;
            }

            List<String> categorias = new ArrayList<>();
            
            if (categoria2.getValue() != null && !categoria2.getValue().equals("Ninguna")) {
                categorias.add(categoria2.getValue());
            }
            
            if (categoria3.getValue() != null && !categoria3.getValue().equals("Ninguna")) {
                categorias.add(categoria3.getValue());
            }
            
            String categoriasAdicionales = categoriaN.getText().trim();
            if (!categoriasAdicionales.isEmpty()) {
                String[] categoriasExtra = categoriasAdicionales.split(",");
                for (String categoria : categoriasExtra) {
                    if (categorias.size() < 4 && !categoria.trim().isEmpty()) {
                        categorias.add(categoria.trim());
                    }
                }
            }

            if (categorias.isEmpty()) {
                mostrarAlerta("Error", "Debe seleccionar al menos una categoría o escribir una nueva", AlertType.ERROR);
                return;
            }
            
            StringBuilder categoriasSQL = new StringBuilder("Categoria_tab_t(");
            for (int i = 0; i < categorias.size(); i++) {
                if (i > 0) {
                    categoriasSQL.append(", ");
                }
                categoriasSQL.append("Categoria_t('").append(categorias.get(i)).append("')");
            }
            categoriasSQL.append(")");

            int libroId = Integer.parseInt(id.getText());
            int numCopias = Integer.parseInt(cantidad.getText());

            String imagePath = guardarImagen(libroId);
            if (imagePath == null) {
                imagePath = "images/libros/default.png";
            }
            
            boolean exitoOracle = conexionOracle.subirLibro(
                libroId,
                nombre.getText(),
                autor.getText(),
                editorial.getText(),
                numCopias,
                sinopsis.getText(),
                categoriasSQL.toString()
            );

            boolean exitoMongo = conexionMongo.guardarDireccionImagen("imagenes-libro", imagePath, libroId);
            
            if (exitoOracle && exitoMongo) {
                mostrarAlerta("Éxito", "Libro agregado correctamente", AlertType.INFORMATION);
                limpiarCampos();
                selectedImageFile = null;
            } else {
                mostrarAlerta("Error", "No se pudo agregar el libro", AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID y la cantidad deben ser números", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el libro: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        id.clear();
        nombre.clear();
        autor.clear();
        editorial.clear();
        cantidad.clear();
        sinopsis.clear();
        categoria2.setValue(null);
        categoria3.setValue(null);
        categoriaN.clear();
        imageView.setImage(null);
    }
    
    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
