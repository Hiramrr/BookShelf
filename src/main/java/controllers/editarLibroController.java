package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class editarLibroController {
    private Usuario usuarioActual;

    @FXML
    private TextField autor;

    @FXML
    private TextField cantidad;

    @FXML
    private TextField categoriaNueva;

    @FXML
    private ComboBox<?> categorias;

    @FXML
    private ComboBox<?> categorias1;

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

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }


    @FXML
    void handleAgregarImagen(ActionEvent event) {

    }

    @FXML
    void handleEditarLibro(ActionEvent event) {

    }

}

