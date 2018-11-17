package com.ums.pau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage rootStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/landing.fxml"));
        primaryStage.setTitle("UMS Portal");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/pictures/ico.jpg")));
        Scene newScene = new Scene(root);
        newScene.getStylesheets().add(getClass().getResource("resources/stylesheet/style.css").toExternalForm());
        primaryStage.setScene(newScene);
        primaryStage.setResizable(false);
        rootStage = primaryStage;
        primaryStage.show();
    }
    public static void main (String[]args){
        launch(args);
    }
}
