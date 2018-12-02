package com.ums.pau.resources;

import com.ums.pau.Main;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderBar implements Initializable {

    public ToolBar headerBar;
    public Label title;
    public Label icon;
    private Stage mainStage = Main.rootStage;
    public void closeWindow() { mainStage.close(); }
    public void minimizeWindow() { mainStage.setIconified(true); }

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setStyle("-fx-font-family: LOBSTER; -fx-font-size: 20");
        icon.setStyle("-fx-font-family: Ubuntu; -fx-font-size: 25");

        headerBar.setOnMousePressed(event -> {
            xOffset = mainStage.getX() - event.getScreenX();
            yOffset = mainStage.getY() - event.getScreenY();
        });
        headerBar.setOnMouseDragged(event -> {
            mainStage.setX(event.getScreenX() + xOffset);
            mainStage.setY(event.getScreenY() + yOffset);
        });
    }
}
