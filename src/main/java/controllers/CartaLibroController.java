package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartaLibroController {

    @FXML private Label cartaNombre;
    @FXML private Label cartaAutor;
    @FXML private ImageView imagen;

    public void setLibro(Libro libro) {
        cartaNombre.setText(libro.getTitulo());
        cartaAutor.setText(libro.getAutor());
        
        try {
            Image imagenLibro = new Image(getClass().getResourceAsStream("/" + libro.getUrlImagen()));
            imagen.setImage(imagenLibro);
        } catch (Exception e) {
            Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
            imagen.setImage(imagenDefault);
        }
    }


}
