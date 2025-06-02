package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalContenedorController {

    BaseDatosOracle conexion;
    private Usuario usuarioActual;

    @FXML
    private Pane contenedor;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarVistaLibros();
    }

    private void cargarVistaLibros() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal_libros.fxml"));
            Parent root = loader.load();
            principal_librosController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        conexion = new BaseDatosOracle();
    }

    @FXML
    void handleCuenta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pagina_usuario.fxml"));
            Parent root = loader.load();
            User_Controller controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEditarLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editar_libro.fxml"));
            Parent root = loader.load();
            editarLibroController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleReseñasRecientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReseñasRecientes.fxml"));
            Parent root = loader.load();
            System.out.println();
            ReseñasRecientesController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePrestamo(ActionEvent event) {
    }

    @FXML
    void handlePrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal_libros.fxml"));
            Parent root = loader.load();
            principal_librosController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSubirLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/subir_libro.fxml"));
            Parent root = loader.load();
            subirLibroController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUsuarios(ActionEvent event) {

    }
}
