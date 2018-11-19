package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;


public class Controller {

    @FXML
    private TextArea editorArea;

    @FXML
    protected void newFile(ActionEvent actionEvent){

        if(editorArea.getLength()>0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Notepad");
            alert.setHeaderText("Do you want save this file?");

            ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.YES);
            ButtonType dntSaveBtn = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveBtn,dntSaveBtn,cancelBtn);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == saveBtn){

            } else if(result.get() == dntSaveBtn) {
                editorArea.clear();
            }

        } else {
        }
    }

    public void exit(ActionEvent actionEvent) {
        if(editorArea.getLength()>0) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if( alert.getResult() == ButtonType.YES )
            Platform.exit();
    }

    //TODO Before Create/Open/Close check if file has been modificated/saved. If not show alert to confirm option.
    //TODO Save/Open method.
}
