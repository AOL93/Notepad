package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Untitle - NotePad v0.1");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setOnCloseRequest(event -> {
            if(controller.exit()) {
                Platform.exit();
            } else {
                event.consume();
            }
        });
        controller.setPrimaryStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
