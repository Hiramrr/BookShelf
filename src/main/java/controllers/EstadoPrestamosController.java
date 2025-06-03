package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;

public class EstadoPrestamosController {

    @FXML
    private TableColumn<Prestamo, String> columnaDevolucion;

    @FXML
    private TableColumn<Prestamo, String> columnaEstado;

    @FXML
    private TableColumn<Prestamo, Integer> columnaId;

    @FXML
    private TableColumn<Prestamo, String> columnaLibro;

    @FXML
    private TableColumn<Prestamo, String> columnaSolicitud;

    @FXML
    private TextField idPrestamo;

    @FXML
    private TableView<Prestamo> tablaPrestamos;

    private Usuario usuarioActual;
    private BaseDatosMongo conexionMongo;
    private BaseDatosOracle conexionOracle;

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();
        
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaLibro.setCellValueFactory(cellData -> {
            int idLibro = cellData.getValue().getId_libro();
            Libro libro = conexionOracle.obtenerLibroPorId(idLibro);
            return new SimpleStringProperty(libro != null ? libro.getTitulo() : "");
        });
        columnaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));
        columnaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fecha_devolucion"));
        columnaEstado.setCellValueFactory(cellData -> {
            int estado = cellData.getValue().getEstado();
            return new SimpleStringProperty(estado == 1 ? "Entregado" : "Pendiente");
        });

        tablaPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idPrestamo.setText(String.valueOf(newSelection.getId()));
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarPrestamos();
    }

    private void cargarPrestamos() {
        if (usuarioActual != null) {
            List<Prestamo> prestamos = conexionOracle.obtenerPrestamos(usuarioActual.getId());
            tablaPrestamos.setItems(FXCollections.observableArrayList(prestamos));
        }
    }

    @FXML
    void handleMarcarEntregado(ActionEvent event) {
        if (idPrestamo.getText().isEmpty()) {
            mensajeError("Error", "Por favor ingrese el ID del préstamo");
            return;
        }

        try {
            int id = Integer.parseInt(idPrestamo.getText());
            
            boolean prestamoValido = false;
            for (Prestamo prestamo : tablaPrestamos.getItems()) {
                if (prestamo.getId() == id) {
                    if (prestamo.getEstado() == 1) {
                        mensajeError("Error", "Este préstamo ya ha sido marcado como entregado");
                        return;
                    }
                    prestamoValido = true;
                    break;
                }
            }

            if (!prestamoValido) {
                mensajeError("Error", "El ID del préstamo no es válido");
                return;
            }

            if (conexionOracle.entregarPrestamo(id)) {
                mensajeBueno("Éxito", "El préstamo ha sido marcado como entregado");
                cargarPrestamos();
                idPrestamo.clear();
            } else {
                mensajeError("Error", "No se pudo marcar el préstamo como entregado");
            }

        } catch (NumberFormatException e) {
            mensajeError("Error", "El ID debe ser un número");
        }
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
