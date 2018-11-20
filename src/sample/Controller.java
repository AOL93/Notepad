package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Optional;


public class Controller {

    @FXML
    private VBox vBox;
    @FXML
    private TextArea editorArea;

    private boolean modified;
    private File fileName;

    /**
     * Open empty notepad after checking if file is saved
     * @param actionEvent
     */
    @FXML
    protected void newFile(ActionEvent actionEvent){
        if(isModified()) {
            switch (saveOnExitAlert()) {
                case 1:
                    saveFile();
                case 0:
                    editorArea.clear();
                    modified=false;
                    fileName = null;
            }
        } else {
            editorArea.clear();
        }
    }

    /**
     * Closing application, but first if modified/unsaved asking about saving(Confirmation Alert).
     */
    @FXML
    protected void exit() {
        if(isModified()) {
            if (saveOnExitAlert()>1)
                saveFile();
        }
        Platform.exit();
    }

    /**
     * Check if file is saved(Confirmation Alert) and then open file chosen by FileChooser.
     */
    @FXML
    protected void openFile() {
        if(isModified()) {
            switch(saveOnExitAlert()) {
                case 1:
                    saveFile();
                case 0:
                    loadFile();
                    break;
            }
        } else {
            loadFile();
        }
    }

    /**
     * Loading file using FileChooser.
     */
    protected void loadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open file");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File file = fc.showOpenDialog(null);
        if(file != null) {
            editorArea.clear();
            try {
                String line;
                BufferedReader br = new BufferedReader(new FileReader(file));
                while((line = br.readLine()) != null) {
                    editorArea.appendText(line + "\n");
                }
                br.close();
                modified=false;
                fileName = file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
    *   Method displays Save Confirmation Alert.
    *   @return positive int value if pressed YES button,
    *           zero if NO button and negative if CANCEL_CLOSE button
    */
    protected int saveOnExitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notepad");
        alert.setHeaderText("Do you want save this file?");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType dntSaveBtn = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveBtn,dntSaveBtn,cancelBtn);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == saveBtn){
            return 1;
        } else if(result.get() == dntSaveBtn) {
            return 0;
        }
        return -1;
    }

    /**
     * Saving file.
     */
    @FXML
    protected void saveFile() {
        if(fileName == null) {
            saveFileAs();
        } else {
            saving(fileName);
        }
    }

    /**
     * Saving file with FileChooser.
     */
    @FXML
    protected void saveFileAs() {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Txt files","*.txt"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files","*.*"));

        File file = fc.showSaveDialog(null);
        saving(file);
    }

    /**
     * Saving content of editorArea to file in param.
     * @param file
     */
    protected void saving(File file) {
        try {
            if(file != null) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(editorArea.getText());
                bw.close();
                System.out.println("Saving to file in PRO VERSION. $5,99");
                fileName = file;
                modified = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changing modified param to true.
     */
    @FXML
    protected void modified() {
        modified=true;
    }

    /**
     * Return boolean value if the file has been modified.
     * @return modified
     */
    protected boolean isModified() {
        return modified;
    }
}
