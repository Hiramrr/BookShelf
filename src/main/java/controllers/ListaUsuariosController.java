package controllers;

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

import java.io.IOException;
import java.util.List;

public class ListaUsuariosController {
    private BaseDatosOracle conexionOracle;
    private Usuario usuarioActual;

    @FXML
    private Pane contenedor;

    @FXML
    private FlowPane flowPaneUsuarios;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        conexionOracle = new BaseDatosOracle();
        conexionOracle.conectar();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<Usuario> listaUsuarios = conexionOracle.obtenerTodosLosUsuarios();
        for (Usuario usuario : listaUsuarios) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartaUsuario.fxml"));
                Node card = loader.load();
                FlowPane.setMargin(card, new Insets(10));

                CartaUsuarioController controller = loader.getController();
                controller.setUsuario(usuario);

                card.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        mostrarPerfilUsuario(usuario);
                    }
                });

                flowPaneUsuarios.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarPerfilUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pagina_usuario.fxml"));
            Parent pane = loader.load();
            User_Controller controller = loader.getController();
            controller.setUsuarioVista(usuarioActual, usuario);
            Stage stage = new Stage();
            stage.setTitle("Perfil de " + usuario.getNombre());
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 