package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class PrestamoController {

    @FXML
    private Pane contenedor;

    @FXML
    private DatePicker fechaSolicitud;

    @FXML
    private DatePicker fechaDevolucion;

    @FXML
    private ComboBox<Libro> libro;

    private Usuario usuarioActual;
    private BaseDatosMongo conexionMongo;
    private BaseDatosOracle conexionOracle;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionMongo = new BaseDatosMongo();
        conexionOracle.conectar();
        fechaSolicitud.setValue(LocalDate.now());
        cargarDatos();
    }

    @FXML
    void handleSolicitarPrestamo(ActionEvent event) {
        if (libro.getValue() == null) {
            mensajeError("Error", "Por favor seleccione un libro");
            return;
        }
        if(libro.getValue().getNum_copias() == 0){
            mensajeError("Lo sentimos", "Lo sentimos, no hay stock disponible del libro seleccionado");
            return;
        }
        if (fechaDevolucion.getValue() == null) {
            mensajeError("Error", "Por favor seleccione una fecha de devolucion");
            return;
        }
        LocalDate fechaActual = LocalDate.now();
        if (fechaDevolucion.getValue().isBefore(fechaActual)) {
            mensajeError("Error", "La fecha de devolucion no puede ser anterior a la fecha actual");
            return;
        }
        try {
            int id_prestamo = generarID();
            int id_libro = libro.getValue().getId();
            Date fecha_solicitud = Date.valueOf(fechaSolicitud.getValue());
            Date fecha_devolucion = Date.valueOf(fechaDevolucion.getValue());
            
            boolean seAñadio = conexionOracle.solicitarPrestamo(id_prestamo, fecha_solicitud, fecha_devolucion, usuarioActual.getId(), id_libro);
            if(seAñadio){
                conexionOracle.restarLibro(id_libro);
                mensajeBueno("Éxito", "¡Se realizó el préstamo correctamente!");
                return;
            }
            mensajeError("Error", "No se pudo procesar el préstamo");
        } catch (Exception e) {
            mensajeError("Error", "Ocurrió un error al procesar el préstamo: " + e.getMessage());
        }
    }

    @FXML
    void handleVerPrestamos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EstadoPrestamos.fxml"));
            Parent pane = loader.load();
            EstadoPrestamosController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            Stage stage = new Stage();
            stage.setTitle("Prestamos");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int generarID(){
        Random random = new Random();
        int limiteInferior = 10000;
        int limiteSuperior = 65535;
        int limiteSuperiorAbierto = limiteSuperior + 1;
        int numeroAleatorio = limiteInferior + random.nextInt(limiteSuperiorAbierto - limiteInferior);

        return numeroAleatorio;
    }

    public void cargarDatos() {
        try {
            List<Libro> listaLibros = conexionOracle.obtener_libros();

            ComboBox<Libro> librosCombo = (ComboBox<Libro>) libro;
            librosCombo.setItems(FXCollections.observableArrayList(listaLibros));

            librosCombo.setCellFactory(lv -> new ListCell<Libro>() {
                @Override
                protected void updateItem(Libro libro, boolean empty) {
                    super.updateItem(libro, empty);
                    setText(empty ? null : libro.getTitulo());
                }
            });

            librosCombo.setButtonCell(new ListCell<Libro>() {
                @Override
                protected void updateItem(Libro libro, boolean empty) {
                    super.updateItem(libro, empty);
                    setText(empty ? null : libro.getTitulo());
                }
            });

        } catch (Exception e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void mensajeError(String titulo, String mensaje) {
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
