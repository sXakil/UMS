package com.ums.pau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static Stage rootStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/splash.fxml"));
        Scene newScene = new Scene(root);
        newScene.getStylesheets().add(getClass().getResource("resources/stylesheet/style.css").toExternalForm());
        primaryStage.setScene(newScene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("bin/pictures/ico.jpg")));
        primaryStage.centerOnScreen();
        rootStage = primaryStage;
        primaryStage.show();
    }

    public static void main (String[]args) {
        launch(args);
    }
}
