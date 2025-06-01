package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CartaLibroController {

    @FXML private Label cartaNombre;
    @FXML private Label cartaAutor;
    @FXML private ImageView imagen;

    public void setLibro(Libro libro) {
        cartaNombre.setText(libro.getTitulo());
        cartaAutor.setText(libro.getAutor());
        
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + libro.getUrlImagen());
            
            if (inputStream == null) {
                File srcFile = new File("src/main/resources/" + libro.getUrlImagen());
                if (srcFile.exists()) {
                    inputStream = new FileInputStream(srcFile);
                }
            }
            
            if (inputStream != null) {
                Image imagenLibro = new Image(inputStream);
                imagen.setImage(imagenLibro);
                inputStream.close();
            } else {
                Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
                imagen.setImage(imagenDefault);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen del libro: " + e.getMessage());
            try {
                Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
                imagen.setImage(imagenDefault);
            } catch (Exception ex) {
                System.err.println("Error al cargar imagen por defecto: " + ex.getMessage());
            }
        }
    }
}
