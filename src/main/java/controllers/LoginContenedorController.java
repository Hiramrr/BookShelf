package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class LoginContenedorController {
    BaseDatosOracle conexion;
    @FXML
    private Button txtUsername;

    @FXML
    private Pane contenedor;

    @FXML
    private Button boton;

    //Este sera falso cuando el boton dice crear cuenta, verdadero cuando dira iniciar sesion, espero no confundirme jaja
    boolean estado = false;

    @FXML
    public void initialize() {
        conexion = new BaseDatosOracle();
        conexion.conectar();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_panel.fxml"));
            Parent root = loader.load();
            contenedor.getChildren().clear();
            contenedor.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleCargar(ActionEvent event) {
        cargarRegistro();
    }

    void cargarRegistro(){
        if(!estado){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Crear_cuenta.fxml"));
                Parent root = loader.load();
                contenedor.getChildren().clear();
                contenedor.getChildren().add(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
            estado = true;
            boton.setText("Iniciar sesión");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_panel.fxml"));
                Parent root = loader.load();
                contenedor.getChildren().clear();
                contenedor.getChildren().add(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
            estado = false;
            boton.setText("Crear cuenta");

        }
    }
}
