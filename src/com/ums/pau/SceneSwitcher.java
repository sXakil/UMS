package com.ums.pau;


import animatefx.animation.FadeIn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    public void switchSceneTo(String scenePath) throws IOException {

        Parent parent = FXMLLoader.load(SceneSwitcher.class.getResource(scenePath));
        Scene scene = new Scene(parent);
        Stage stage = Main.rootStage;
        stage.setScene(scene);
        stage.show();
        new FadeIn(parent).play();
    }
}