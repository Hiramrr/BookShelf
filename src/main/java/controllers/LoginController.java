package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class LoginController {
    BaseDatosOracle conexion;

    @FXML
    private PasswordField contraseña;

    @FXML
    private TextField correo;

    @FXML
    private Button iniciar;

    String CorreoUser;

    String ContraseñaUser;


    @FXML
    public void initialize() {
        conexion = new BaseDatosOracle();
        conexion.conectar();
    }

    @FXML
    void handleLogin(ActionEvent event) {
        CorreoUser = correo.getText();
        ContraseñaUser = contraseña.getText();
        if(!estaLleno(CorreoUser,ContraseñaUser)){
            return;
        }
        System.out.println(CorreoUser + ContraseñaUser);
        if(!conexion.consultarUsuario(CorreoUser, ContraseñaUser)){
            mensajeError("Credenciales invalidas", "Algo no es correcto");
            return;
        }
        mensajeBueno("Se inicio sesion de forma correcta", "Bienvenido de vuelta");
    }

    boolean estaLleno(String CorreoUser, String ContraseñaUser){
        if(CorreoUser.equals("") && ContraseñaUser.equals("")){
            mensajeError("Por favor llena todos los campos", "Por favor llena todos los campos");
            return false;
        }
        else if(CorreoUser.equals("")){
            mensajeError("Por favor introduce el correo", "Por favor introduce tu correo");
            return false;
        }
        else if(ContraseñaUser.equals("")){
            mensajeError("Por favor introduce la contraseña", "Por favor introduce tu contraseña");
            return false;
        }
        return true;
    }

    void mensajeError(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    void mensajeBueno(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
