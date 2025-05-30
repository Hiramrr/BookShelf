package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import java.io.IOException;
import java.util.List;

public class ReseñasRecientesController {

    @FXML
    private Pane contenedor;

    @FXML
    private FlowPane flowPanelReseñas;

    private Usuario usuarioActual;
    private BaseDatosMongo conexionMongo;
    private BaseDatosOracle conexionOracle;

    public ReseñasRecientesController() {
        conexionMongo = new BaseDatosMongo();
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();
    }


    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarReseñasRecientes();
    }

    @FXML
    public void cargarReseñasRecientes() {
        List<Document> listaReseñas = conexionMongo.obtenerReseñasRecientes("libro-reseña");
        for (Document reseña : listaReseñas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartaReseña.fxml"));
                Node card = loader.load();
                FlowPane.setMargin(card, new Insets(10));

                CartaReseñaController controller = loader.getController();
                controller.setDocumento(reseña);

                card.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                       mostrarDetallesLibro(reseña.getInteger("id_libro"));
                    }
                });

                flowPanelReseñas.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void mostrarDetallesLibro(int idLibro){
        Libro libro = conexionOracle.obtenerLibroPorId(idLibro);
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
}
