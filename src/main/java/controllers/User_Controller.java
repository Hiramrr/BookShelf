package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class User_Controller {

    @FXML
    private Pane contenedor;

    @FXML
    private FlowPane flowPanePrestamo;

    @FXML
    private FlowPane flowPaneReseñas;

    @FXML
    private Text nombreUser;

    @FXML
    private Label aboutme;

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        nombreUser.setText("Hola " + usuarioActual.getNombre() + "!");
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_contenedor.fxml"));
            Parent pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("BookShelf");
            stage.setScene(new Scene(pane));
            Stage currentStage = (Stage) contenedor.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEditarPerfil(ActionEvent event) {

    }


}
