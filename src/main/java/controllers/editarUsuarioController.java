package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class editarUsuarioController {
    private Usuario usuarioActual;
    private Libro libroFavoritoActual;
    BaseDatosOracle conexionOracle;
    BaseDatosMongo conexionMongo;

    @FXML
    private TextArea aboutMe;

    @FXML
    private ComboBox<Libro> libroFavorito;

    @FXML
    private TextField nombre;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        
    }

    public void setAboutMe(String sobreMi){
        aboutMe.setText(sobreMi);
    }

    public void setLibroSeleccionado(Libro libro) {
        this.libroFavoritoActual = libro;
        cargarDatos();
    }

    @FXML
    void handleActualizar(ActionEvent event) {
        try {
            if (nombre.getText().isEmpty() || aboutMe.getText().isEmpty() ||
                libroFavorito.getValue() == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            boolean exitoOracle = conexionOracle.editarNombreUsuario(usuarioActual.getId(), nombre.getText());

            boolean exitoMongo = conexionMongo.guardarAboutMe("usuario-info", aboutMe.getText(), libroFavorito.getValue().getTitulo(), usuarioActual.getId(), libroFavorito.getValue().getId());

            if (exitoOracle && exitoMongo) {
                mostrarAlerta("Éxito", "Información actualizada correctamente", Alert.AlertType.INFORMATION);
                usuarioActual.setNombre(nombre.getText());
                Stage currentStage = (Stage) aboutMe.getScene().getWindow();
                currentStage.close();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la información", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionMongo = new BaseDatosMongo();
        conexionOracle.conectar();
    }

    public void cargarDatos() {
        nombre.setText(usuarioActual.getNombre());

        try {
            List<Libro> listaLibros = conexionOracle.obtener_libros();

            ComboBox<Libro> librosCombo = (ComboBox<Libro>) libroFavorito;
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

            if (libroFavoritoActual != null) {
                for (Libro libro : listaLibros) {
                    if (libro.getTitulo().equals(libroFavoritoActual.getTitulo())) {
                        librosCombo.setValue(libro);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
