package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_contenedor.fxml"));
        AnchorPane load = loader.load();

        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("BookShelf");

    }
}