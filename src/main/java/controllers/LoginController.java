package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
        String correoUser = correo.getText();
        String contraseñaUser = contraseña.getText();

        if (estaLleno(correoUser, contraseñaUser)) {
            Usuario usuario = conexion.consultarUsuario(correoUser, contraseñaUser);
            if (usuario != null) {
                cargarPrincipal(usuario);
            } else {
                mensajeError("Error de inicio de sesión", "Correo o contraseña incorrectos");
            }
        }
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

    void cargarPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal_contenedor.fxml"));
            Parent pane = loader.load();
            PrincipalContenedorController controller = loader.getController();
            controller.setUsuario(usuario);
            
            Stage stage = new Stage();
            stage.setTitle("BookShelf");
            stage.setScene(new Scene(pane));
            Stage currentStage = (Stage) iniciar.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
