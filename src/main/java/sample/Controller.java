package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.FontSelectorDialog;

import java.io.*;
import java.util.Optional;


public class Controller implements PrimaryStageAware {

    @FXML
    private TextArea editorArea;
    @FXML
    private CheckMenuItem wrapCheck;

    private boolean modified;
    private File fileName;
    private String windowTitle;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        modified=false;

        editorArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!isModified()) {
                modified = true;
                primaryStage.setTitle("*" + getTitle());
            }
        });
    }

    /**
     * Open empty notepad after checking if file is saved
     */
    @FXML
    protected void newFile(){
        if(isModified()) {
            switch (saveOnExitAlert()) {
                case 1:
                    saveFile();
                case 0:
                    editorArea.clear();
                    modified=false;
                    fileName = null;
                    primaryStage.setTitle(getTitle());
            }
        } else {
            editorArea.clear();
        }
    }

    /**
     * Closing application, but first if modified/unsaved asking about saving(Confirmation Alert).
     */
    @FXML
    public boolean exit() {
        if(isModified()) {
            switch(saveOnExitAlert()) {
                case 1:
                    saveFile();
                case 0:
                    Platform.exit();
                    break;
                default:
                    return false;
            }
        }
        return true;
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
                primaryStage.setTitle(getTitle());
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
                //System.out.println("Saving to file in PRO VERSION. $5,99");
                fileName = file;
                modified = false;
                primaryStage.setTitle(getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return boolean value if the file has been modified.
     * @return modified
     */
    protected boolean isModified() {
        return modified;
    }

    /**
     * Printing content form editorArea.
     */
    @FXML
    public void printFile() {
        FilePrinter filePrinter = new FilePrinter();
        filePrinter.print(editorArea);
    }

    /**
     *  Return window Title. For new projects name is "Untitled - Notepad v0.0.1"
     *  but if file is opened or save name title return file name with " - Notepad v0.0.1".
     * @return Name of window.
     */
    public String getTitle() {
        windowTitle = fileName==null ? "Untitled" : fileName.getName();
        windowTitle += " - Notepad v0.0.1";

        return  windowTitle;
    }

    /**
     *  Display Font Selector Dialog and changing the editorArea font to selected.
     */
    public void chooseFont() {
        FontSelectorDialog fontSelectorDialog = new FontSelectorDialog(editorArea.getFont());

        fontSelectorDialog.setTitle("Choose font");
        Optional<Font> font = fontSelectorDialog.showAndWait();

        font.ifPresent(font1 -> editorArea.setFont(font1));
    }

    /**
     * Changing wrap property for editorArea.
     */
    public void setWrapText() {
        if( wrapCheck.isSelected() ) {
            editorArea.setWrapText(true);
        } else {
            editorArea.setWrapText(false);
        }
    }

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}