package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CartaUsuarioController {
    @FXML
    private Label nombreUsuario;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        nombreUsuario.setText(usuario.getNombre());
    }

    public Usuario getUsuario() {
        return usuario;
    }
} 