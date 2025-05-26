package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Random;

public class CrearCuentaController {
    BaseDatosOracle conexion;
    @FXML
    private PasswordField contraseña;

    @FXML
    private TextField correo;

    @FXML
    private Button crear;

    @FXML
    private TextField usuario;

    @FXML
    public void initialize() {
        conexion = new BaseDatosOracle();
        conexion.conectar();
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo.matches(regex);
    }

    @FXML
    void handleCrearCuenta(ActionEvent event) {
        int id = generarID();
        String nombreUser = usuario.getText();
        String correoUser = correo.getText();
        String contraseñaUser = contraseña.getText();

        if (!estaLleno(nombreUser, correoUser, contraseñaUser)) {
            return;
        }

        if (!esCorreoValido(correoUser)) {
            mensajeError("Correo inválido", "Por favor introduce un correo electrónico válido");
            return;
        }

        try {
            conexion.registroUsuario(id, nombreUser, correoUser, contraseñaUser);
            mensajeBueno("Cuenta creada", "¡Tu cuenta ha sido creada exitosamente!");
            limpiarCampos();
        } catch (Exception e) {
            mensajeError("Error", "No se pudo crear la cuenta. Por favor intenta nuevamente.");
        }
    }

    private void limpiarCampos() {
        usuario.setText("");
        correo.setText("");
        contraseña.setText("");
    }

    boolean estaLleno(String nombreUser, String CorreoUser, String ContraseñaUser) {
        if(nombreUser.equals("") && CorreoUser.equals("") && ContraseñaUser.equals("")){
            mensajeError("Por favor llena todos los campos", "Por favor llena todos los campos");
            return false;
        }

        if(nombreUser.equals("")){
            mensajeError("Por favor introduce tu nombre", "Por favor introduce tu nombre");
            return false;
        }
        if(CorreoUser.equals("")){
            mensajeError("Por favor introduce el correo", "Por favor introduce tu correo");
            return false;
        }
        if(ContraseñaUser.equals("")){
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

    public int generarID(){
        Random random = new Random();
        int limiteInferior = 10000;
        int limiteSuperior = 65535;
        int limiteSuperiorAbierto = limiteSuperior + 1;
        int numeroAleatorio = limiteInferior + random.nextInt(limiteSuperiorAbierto - limiteInferior);

        return numeroAleatorio;
    }
}
