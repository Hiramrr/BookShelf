package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class principal_librosController {
    private BaseDatosOracle conexionOracle;
    BaseDatosMongo conexionMongo;
    private List<Libro> todosLosLibros;
    private Usuario usuarioActual;

    @FXML
    private TextField busquedaField;

    @FXML
    private Pane contenedor;

    @FXML
    private FlowPane flowPaneLibros;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();

        conexionMongo = new BaseDatosMongo();
        todosLosLibros = conexionOracle.obtener_libros();
        mostrarLibros(todosLosLibros);
        
        busquedaField.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarLibro(newValue);
        });
    }

    private void buscarLibro(String busqueda) {
        String filtro = busqueda.trim().toLowerCase();

        List<Libro> filtrados = todosLosLibros.stream()
                .filter(libro -> 
                    libro.getTitulo().toLowerCase().contains(filtro) ||
                    libro.getAutor().toLowerCase().contains(filtro) ||
                    libro.getEditorial().toLowerCase().contains(filtro) ||
                    libro.getCategoria().stream()
                        .anyMatch(cat -> cat.getNombre().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            mostrarMensajeError("Sin resultados", "No se encontraron libros que coincidan con la búsqueda.");
        }

        mostrarLibros(filtrados);
    }

    private void mostrarLibros(List<Libro> libros) {
        flowPaneLibros.getChildren().clear();

        for (Libro libro : libros) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartasLibros.fxml"));
                Node card = loader.load();
                FlowPane.setMargin(card, new Insets(10));
                
                CartaLibroController controller = loader.getController();
                controller.setLibro(libro);

                card.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        mostrarDetallesLibro(libro);
                    }
                });
                
                flowPaneLibros.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarDetallesLibro(Libro libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetallesLibro.fxml"));
            Parent pane = loader.load();
            DetallesLibroController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            controller.setLibro(libro);
            Stage stage = new Stage();
            stage.setTitle("BookShelf");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensajeError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}