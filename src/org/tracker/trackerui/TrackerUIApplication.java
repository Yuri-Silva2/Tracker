package org.tracker.trackerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TrackerUIApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TrackerUIApplication.class.getResource("TrackerUI.fxml"));
            Pane layout = fxmlLoader.load();

            Scene scene = new Scene(layout, 300.0D, 200.0D);

            stage.setResizable(false);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
