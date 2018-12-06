package com.ums.pau.resources;

import com.ums.pau.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ForgottenPasswordPrompt {
    private static Stage thisStage;

    public void closeWindow() {
        Main.rootStage.setOpacity(1);
        thisStage.close();
    }

    public void show() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("forgottenPassPrompt.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Password Recovery");
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.rootStage);
        Main.rootStage.setOpacity(0.8);
        thisStage = stage;
        stage.show();
    }
}