package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bson.Document;

import javax.print.Doc;

public class CartaReseñaController {
    BaseDatosOracle conexionOracle;
    @FXML
    private Label CartaEstrellas;

    @FXML
    private Label CartaUsuario;

    @FXML
    private Label cartaNombreLibro;

    @FXML
    private Label cartaReseña;

    @FXML
    private ImageView imagen;

    Usuario usuarioActual;

    Document documento;

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void setDocumento(Document documento){
        this.documento = documento;
        cargarDatos();
    }

    public void setLibro(Libro libro) {
        cartaNombreLibro.setText(libro.getTitulo());
        try {
            Image imagenLibro = new Image(getClass().getResourceAsStream("/" + libro.getUrlImagen()));
            imagen.setImage(imagenLibro);
        } catch (Exception e) {
            Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
            imagen.setImage(imagenDefault);
        }
    }

    public void cargarDatos(){
        String estrellas = documento.getString("Estrellas");
        String reseña = documento.getString("Reseña");
        int idUsuario = documento.getInteger("id_usuario");
        int idLibro = documento.getInteger("id_libro");
        CartaEstrellas.setText(estrellas);
        cartaReseña.setText(reseña);
        String nombreUsuario = conexionOracle.obtenerNombreUsuario(idUsuario);
        CartaUsuario.setText(nombreUsuario);
        Libro libro = conexionOracle.obtenerLibroPorId(idLibro);
        setLibro(libro);
    }
}
