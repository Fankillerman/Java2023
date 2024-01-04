package org.starmap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.starmap.controller.StarMapController;
import org.starmap.view.StarMapView;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        StarMapController controller = new StarMapController("src/main/resources/stars.json");
        StarMapView view = new StarMapView(controller);

        BorderPane root = new BorderPane();
        root.setCenter(view);
        root.setRight(view.getControlPanel());

        Scene scene = new Scene(root, 1200, 768);
        primaryStage.setTitle("Star Map");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            controller.saveData("src/main/resources/stars.json");
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
