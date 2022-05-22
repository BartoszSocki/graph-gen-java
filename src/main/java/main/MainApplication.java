package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth()/2, screenBounds.getHeight()/2);
        double sceneMinHeight = 500;
        double sceneMinWidth = 700;

        stage.setTitle("Graphalgo-gui");
        stage.setScene(scene);
        stage.setMinHeight(sceneMinHeight);
        stage.setMinWidth(sceneMinWidth);
        stage.show();
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("icon.jpg")));
    }

    public static void main(String[] args) {
        launch();
    }
}