package com.ums.pau.resources;

import com.ums.pau.Main;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderBar implements Initializable {

    public ToolBar headerBar;
    private Stage mainStage = Main.rootStage;
    public void closeWindow() { mainStage.close(); }
    public void minimizeWindow() { mainStage.setIconified(true); }

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
