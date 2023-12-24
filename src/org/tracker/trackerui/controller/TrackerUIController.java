package org.tracker.trackerui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tracker.Tracker;
import org.tracker.report.ReportType;
import org.tracker.trackerui.TrackerUIApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackerUIController {

    public Text total_value;

    public Button fafButton;
    public Button presumedButton;
    public Button optionsButton;

    private final List<File> presumedFiles = new ArrayList<>();
    private final List<File> fafFiles = new ArrayList<>();

    private boolean selectedPresumedFile = false;
    private boolean selectedFafFile = false;

    public void onFafButtonClicked(MouseEvent event) {
        if (!selectedFafFile) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            List<File> files = createFileChooser(stage);
            if (files == null) return;

            fafFiles.addAll(files);

            if (fafFiles.isEmpty()) return;

            fafButton.setText("Gerar");
            selectedFafFile = true;
            optionsButton.setDisable(true);
            presumedButton.setDisable(true);

        } else {
            Tracker.createQueue(fafFiles);
            Tracker.initialize(ReportType.FAF);
            fafButton.setText("Selecionar");
            selectedFafFile = false;
            optionsButton.setDisable(false);
            presumedButton.setDisable(false);
            updateTotalValue();
        }
    }

    public void onPresumedButtonClicked(MouseEvent event) {
        if (!selectedPresumedFile) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            List<File> files = createFileChooser(stage);
            if (files == null) return;

            presumedFiles.addAll(files);

            if (presumedFiles.isEmpty()) return;

            presumedButton.setText("Gerar");
            selectedPresumedFile = true;
            optionsButton.setDisable(true);
            fafButton.setDisable(true);

        } else {
            Tracker.createQueue(presumedFiles);
            Tracker.initialize(ReportType.PRESUMED);
            presumedButton.setText("Selecionar");
            selectedPresumedFile = false;
            optionsButton.setDisable(false);
            fafButton.setDisable(false);
            updateTotalValue();
        }
    }

    public void onOptionsButtonClicked(MouseEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TrackerUIApplication.class.getResource("OptionsUI.fxml"));
        Pane layout = fxmlLoader.load();

        Scene scene = new Scene(layout, 600.0, 360.0);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private List<File> createFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivos Excel", "*.xlsx", "*.xls"));

        return fileChooser.showOpenMultipleDialog(stage);
    }

    private void updateTotalValue() {
        total_value.setText(String.valueOf(Tracker.getArchivesValue())
                .replace(".", ","));
    }
}
