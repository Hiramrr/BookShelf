package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class DetallesLibroController {
    BaseDatosOracle conexionOracle;
    BaseDatosMongo conexionMongo;
    @FXML
    private Label cartaNombre1;

    @FXML
    private Label cartaNombre11;

    @FXML
    private Label cartaNombre111;

    @FXML
    private Pane contenedor;

    @FXML
    private ChoiceBox<Integer> estrellas;

    @FXML
    private ImageView imageView;

    @FXML
    private Label sinopsis;

    @FXML
    private TableView<DatoTabla> tablaDatos;

    @FXML
    private TableColumn<DatoTabla, String> columnaInfo;

    @FXML
    private Label titulo;

    Usuario usuarioActual;
    Libro libroActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void setLibro(Libro libro) {
        this.libroActual = libro;
        cargarLibro();
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();
        conexionMongo = new BaseDatosMongo();
        
        // Configurar la columna
        columnaInfo.setCellValueFactory(cellData -> cellData.getValue().valorProperty());
    }

    public void cargarLibro() {
        titulo.setText(libroActual.getTitulo());
        sinopsis.setText(libroActual.getSinopsis_());

        tablaDatos.setItems(obtenerDatos());
        try {
            Image imagenLibro = new Image(getClass().getResourceAsStream("/" + libroActual.getUrlImagen()));
            imageView.setImage(imagenLibro);
        } catch (Exception e) {
            Image imagenDefault = new Image(getClass().getResourceAsStream("/images/libros/default.png"));
            imageView.setImage(imagenDefault);
        }
    }

    public ObservableList<DatoTabla> obtenerDatos() {
        ObservableList<DatoTabla> datos = FXCollections.observableArrayList();
        datos.add(new DatoTabla("Autor: " + libroActual.getAutor()));
        datos.add(new DatoTabla("Editorial: " + libroActual.getEditorial()));
        datos.add(new DatoTabla("Categorías:"));
        for (Categoria categoria : libroActual.getCategoria()) {
            datos.add(new DatoTabla("   - " + categoria.getNombre()));
        }
        return datos;
    }
}
