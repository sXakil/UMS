package com.ums.pau.resources;

import com.ums.pau.Main;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderBar implements Initializable {
    private static double xOffset = 0;
    private static double yOffset = 0;
    public ToolBar topBar;

    public void closeWindow() {
        Main.rootStage.close();
    }

    public void minimizeWindow() {
        Main.rootStage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage primaryStage = Main.rootStage;
        topBar.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        topBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }
}
