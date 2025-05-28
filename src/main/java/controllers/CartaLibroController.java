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
            System.err.println("Error al cargar la imagen del libro: " + e.getMessage());
            // Cargar imagen por defecto si hay error
            Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
            imagen.setImage(imagenDefault);
        }
    }

}
