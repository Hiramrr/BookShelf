package controllers;

import BaseDatosMongo.BaseDatosMongo;
import BaseDatosOracle.BaseDatosOracle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

public class User_Controller {
    BaseDatosOracle conexionOracle;
    BaseDatosMongo conexionMongo;

    @FXML
    private Pane contenedor;

    @FXML
    private FlowPane flowPaneLibro;

    @FXML
    private FlowPane flowPaneReseñas;

    @FXML
    private Text nombreUser;

    @FXML
    private Label aboutme;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnEditarPerfil;

    private Usuario usuarioActual;
    private Usuario usuarioVista;
    private Libro libroFavorito;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarDetallesUsuario(usuarioVista.getId());
        btnCerrarSesion.setVisible(true);
        btnEditarPerfil.setVisible(true);
        cargarReseñasUser(usuarioVista.getId());
    }

    public void setUsuarioVista(Usuario usuarioActual, Usuario usuarioVista) {
        this.usuarioActual = usuarioActual;
        this.usuarioVista = usuarioVista;
        nombreUser.setText("Perfil de " + usuarioVista.getNombre());
        cargarDetallesUsuario(usuarioVista.getId());
        btnCerrarSesion.setVisible(false);
        btnEditarPerfil.setVisible(false);
        cargarReseñasUser(usuarioVista.getId());
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionMongo = new BaseDatosMongo();
        conexionOracle.conectar();
    }


    public void cargarDetallesUsuario(int id){
        try {
            Document doc_user = conexionMongo.obtenerAboutMe("usuario-info", id);
            int id_libro = doc_user.getInteger("id_libro");
            libroFavorito = conexionOracle.obtenerLibroPorId(id_libro);
            mostrarFavorito(libroFavorito);
            aboutme.setText(doc_user.getString("aboutMe"));
        } catch (Exception e) {
            System.out.println("No debo de hacer nada porque no tiene nada jaja");
        }
    }


    public void mostrarFavorito(Libro libro){
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

            flowPaneLibro.getChildren().add(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_contenedor.fxml"));
            Parent pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("BookShelf");
            stage.setScene(new Scene(pane));
            Stage currentStage = (Stage) contenedor.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEditarPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Editar_datos.fxml"));
            Parent pane = loader.load();
            editarUsuarioController controller = loader.getController();
            controller.setUsuario(usuarioActual);
            controller.setAboutMe(aboutme.getText());
            controller.setLibroSeleccionado(libroFavorito);
            Stage stage = new Stage();
            stage.setTitle("EditarDatos");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    public void cargarReseñasUser(int id) {
        List<Document> listaReseñas = conexionMongo.obtenerReseñaUser("libro-reseña", id);
        for (Document reseña : listaReseñas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartaReseña.fxml"));
                Node card = loader.load();
                FlowPane.setMargin(card, new Insets(10));

                CartaReseñaController controller = loader.getController();
                controller.setDocumento(reseña);

                card.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        mostrarDetallesLibro(conexionOracle.obtenerLibroPorId(reseña.getInteger("id_libro")));
                    }
                });

                flowPaneReseñas.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
