package controllers;

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

public class subirLibroController {

    BaseDatosOracle conexion;

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

    public void setUsuario(Usuario usuario) {
            this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexion = new BaseDatosOracle();
        conexion.conectar();
        cargarCategorias();
    }

    @FXML
    void handleAgregarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void cargarCategorias(){
        ObservableList<Categoria> categorias = conexion.obtenerCategorias();

        categoria2.getItems().add("Ninguna");
        categoria3.getItems().add("Ninguna");

        for (Categoria categoria : categorias) {
            categoria2.getItems().add(categoria.getNombre());
            categoria3.getItems().add(categoria.getNombre());
        }

        categoria2.setValue("Ninguna");
        categoria3.setValue("Ninguna");
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
            
            boolean exito = conexion.subirLibro(libroId, nombre.getText(), autor.getText(), editorial.getText(), numCopias, sinopsis.getText(), categoriasSQL.toString());
            if (exito) {
                mostrarAlerta("Éxito", "Libro agregado correctamente", AlertType.INFORMATION);
                limpiarCampos();
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
