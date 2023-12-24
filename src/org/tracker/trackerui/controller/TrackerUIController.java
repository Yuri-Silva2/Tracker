package org.tracker.trackerui.controller;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tracker.Tracker;
import org.tracker.report.ReportType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrackerUIController {

    public Text total_value;

    public Button fafButton;
    public Button presumedButton;
    public Button generateButton;

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

            selectedFafFile = true;
            presumedButton.setDisable(true);
        }
    }

    public void onPresumedButtonClicked(MouseEvent event) {
        if (!selectedPresumedFile) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            List<File> files = createFileChooser(stage);
            if (files == null) return;

            presumedFiles.addAll(files);

            if (presumedFiles.isEmpty()) return;

            selectedPresumedFile = true;
            fafButton.setDisable(true);
        }
    }

    public void onGenerateClicked(MouseEvent event) {
        if (selectedPresumedFile) {
            Tracker.createQueue(presumedFiles);
            Tracker.initialize(ReportType.PRESUMED);
            presumedButton.setText("Selecionar");
            selectedPresumedFile = false;
            fafButton.setDisable(false);
            updateTotalValue();

        } else if (selectedFafFile) {
            Tracker.createQueue(fafFiles);
            Tracker.initialize(ReportType.FAF);
            fafButton.setText("Selecionar");
            selectedFafFile = false;
            presumedButton.setDisable(false);
            updateTotalValue();

        }
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
