package controllers;

import BaseDatosOracle.BaseDatosOracle;
import BaseDatosMongo.BaseDatosMongo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class editarLibroController {

    @FXML
    private TextField autor;

    @FXML
    private TextField cantidad;

    @FXML
    private TextField categoriaNueva;

    @FXML
    private ComboBox<?> categorias;

    @FXML
    private Pane contenedor;

    @FXML
    private TextField editorial;

    @FXML
    private TextField id;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<?> libro;

    @FXML
    private TextField nombre;

    @FXML
    private TextArea sinopsis;

    private Usuario usuarioActual;

    private File selectedImageFile;
    private static final String portadas_url = "src/main/resources/images/libros/";

    private BaseDatosOracle conexionOracle;
    private BaseDatosMongo conexionMongo;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionMongo = new BaseDatosMongo();
        conexionOracle.conectar();
        cargarDatos();
    }

    @FXML
    void handleEditarLibro(ActionEvent event) {
        try {
            if (id.getText().isEmpty() || nombre.getText().isEmpty() || autor.getText().isEmpty() || 
                editorial.getText().isEmpty() || cantidad.getText().isEmpty() || sinopsis.getText().isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            int libroId = Integer.parseInt(id.getText());
            int numCopias = Integer.parseInt(cantidad.getText());
            
            String imagePath = null;
            if (selectedImageFile != null) {
                imagePath = guardarImagen(libroId);
                if (imagePath != null) {
                    conexionMongo.editarrDireccionImagen("imagenes-libro", imagePath, libroId);
                }
            }

            String categoriaNuevaStr = null;
            String categoriaAntiguaStr = null;

            if (categorias.getValue() != null && !categorias.getValue().toString().equals("Ninguna")) {
                categoriaAntiguaStr = categorias.getValue().toString();
            }
            
            if (categoriaNueva.getText() != null && !categoriaNueva.getText().trim().isEmpty()) {
                categoriaNuevaStr = categoriaNueva.getText().trim();
            }

            boolean exitoOracle = conexionOracle.editarLibro(libroId, nombre.getText(), autor.getText(), editorial.getText(), numCopias, sinopsis.getText(), categoriaAntiguaStr, categoriaNuevaStr);

            if (exitoOracle) {
                mostrarAlerta("Éxito", "Libro actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el libro", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID y la cantidad deben ser números", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar el libro: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
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

    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void cargarDatos() {
        try {
            List<Libro> listaLibros = conexionOracle.obtener_libros();
            
            ComboBox<Libro> librosCombo = (ComboBox<Libro>) libro;
            librosCombo.setItems(FXCollections.observableArrayList(listaLibros));
            
            librosCombo.setCellFactory(lv -> new ListCell<Libro>() {
                @Override
                protected void updateItem(Libro libro, boolean empty) {
                    super.updateItem(libro, empty);
                    setText(empty ? null : libro.getTitulo());
                }
            });
            
            librosCombo.setButtonCell(new ListCell<Libro>() {
                @Override
                protected void updateItem(Libro libro, boolean empty) {
                    super.updateItem(libro, empty);
                    setText(empty ? null : libro.getTitulo());
                }
            });
            
            ComboBox<Categoria> categoriasCombo = (ComboBox<Categoria>) categorias;
            categoriasCombo.setCellFactory(lv -> new ListCell<Categoria>() {
                @Override
                protected void updateItem(Categoria categoria, boolean empty) {
                    super.updateItem(categoria, empty);
                    setText(empty ? null : categoria.getNombre());
                }
            });
            
            categoriasCombo.setButtonCell(new ListCell<Categoria>() {
                @Override
                protected void updateItem(Categoria categoria, boolean empty) {
                    super.updateItem(categoria, empty);
                    setText(empty ? null : categoria.getNombre());
                }
            });
            
            librosCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Libro libroCompleto = conexionOracle.obtenerLibroPorId(newValue.getId());
                    if (libroCompleto != null) {
                        id.setText(String.valueOf(libroCompleto.getId()));
                        nombre.setText(libroCompleto.getTitulo());
                        autor.setText(libroCompleto.getAutor());
                        editorial.setText(libroCompleto.getEditorial());
                        cantidad.setText(String.valueOf(libroCompleto.getNum_copias()));
                        sinopsis.setText(libroCompleto.getSinopsis_());
                        
                        categoriasCombo.getItems().clear();
                        if (libroCompleto.getCategoria() != null) {
                            categoriasCombo.getItems().addAll(libroCompleto.getCategoria());
                            
                            if (!libroCompleto.getCategoria().isEmpty()) {
                                categoriasCombo.setValue(libroCompleto.getCategoria().get(0));
                            }
                        }
                        
                        try {
                            Image imagen = new Image(getClass().getResourceAsStream("/" + libroCompleto.getUrlImagen()));
                            imageView.setImage(imagen);
                        } catch (Exception e) {
                            Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
                            imageView.setImage(imagenDefault);
                        }
                    }
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
