package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class CartaLibroController {

    @FXML private Label cartaNombre;
    @FXML private Label cartaAutor;
    @FXML private ImageView imagen;

    public void setLibro(Libro libro) {
        cartaNombre.setText(libro.getTitulo());
        cartaAutor.setText(libro.getAutor());
    }

}
