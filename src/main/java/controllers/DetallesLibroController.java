package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

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
    private TextArea reseñaField;

    @FXML
    private Pane contenedor;

    @FXML
    private ChoiceBox<String> estrellas;

    @FXML
    private ImageView imageView;

    @FXML
    private Label sinopsis;

    @FXML
    private TableView<DatoTabla> tablaDatos;

    @FXML
    private TableColumn<DatoTabla, String> columnaInfo;

    @FXML
    private TableView<Document> tablaReseñas;

    @FXML
    private TableColumn<Document, String> UsuarioReseña;

    @FXML
    private TableColumn<Document, String> EstrellasReseña;

    @FXML
    private TableColumn<Document, String> ReseñaTexto;

    @FXML
    private Label titulo;

    Usuario usuarioActual;
    Libro libroActual;


    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarChoice();
    }

    public void setLibro(Libro libro) {
        this.libroActual = libro;
        cargarLibro();
        cargarReseñas();
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();
        conexionMongo = new BaseDatosMongo();
        
        columnaInfo.setCellValueFactory(cellData -> cellData.getValue().valorProperty());
        
        UsuarioReseña.setCellValueFactory(data -> {
            int idUsuario = data.getValue().getInteger("id_usuario");
            String nombreUsuario = conexionOracle.obtenerNombreUsuario(idUsuario);
            return new SimpleStringProperty(nombreUsuario != null ? nombreUsuario : "Usuario " + idUsuario);
        });
        EstrellasReseña.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getString("Estrellas")));
        ReseñaTexto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getString("Reseña")));
    }

    void cargarChoice(){
        ObservableList<String> estrella = FXCollections.observableArrayList();
        estrella.add("5★");
        estrella.add("4★");
        estrella.add("3★");
        estrella.add("2★");
        estrella.add("1★");
        estrella.add("0★");
        estrellas.setItems(estrella);
        estrellas.setValue(estrella.get(0));
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

    void cargarReseñas(){
        ObservableList<Document> reseñas = conexionMongo.obtenerReseñas("libro-reseña", libroActual.getId());
        tablaReseñas.setItems(reseñas);
    }

    @FXML
    void handleRegresar(ActionEvent event) {
        Stage currentStage = (Stage) contenedor.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void handleSubirReseña(ActionEvent event){
        if(reseñaField.getText().equals("")){
            mensajeError("Escriba una reseña por favor", "Por favor introduzca una reseña");
            return;
        }

        boolean exito = conexionMongo.guardarReseña("libro-reseña", usuarioActual.getId(), libroActual.getId(), estrellas.getValue(), reseñaField.getText());
        if(exito){
            mensajeBueno("Se subio la reseña con exito!", "Se subio tu reseña con exito!");
            cargarReseñas();
            return;
        }
        mensajeError("Esperemos y nunca salga este error", "hubo un problema al guardar los datos en mongo");
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
}
