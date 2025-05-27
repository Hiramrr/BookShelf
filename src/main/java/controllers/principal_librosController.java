package controllers;

import BaseDatosOracle.BaseDatosOracle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class principal_librosController {
    private BaseDatosOracle bd;
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
        bd = new BaseDatosOracle();
        bd.conectar();
        todosLosLibros = bd.obtener_libros();
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
        System.out.println(libro.getId());
        System.out.println(libro.getTitulo());
        System.out.println(libro.getAutor());
        List<Categoria> categoria = new ArrayList<>(libro.getCategoria());
        System.out.println(categoria.get(0).getNombre());
        System.out.println(libro.getEditorial());
        System.out.println(libro.getNum_copias());
        System.out.println(libro.getSinopsis_());
    }

    private void mostrarMensajeError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}