package com.ums.pau.resources;

import com.ums.pau.Main;
import com.ums.pau.Shake;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderBar implements Initializable {
    @FXML
    private ToolBar headerBar;
    @FXML
    private Label title;
    @FXML
    private Label icon;
    @FXML
    private Button minimize, close;

    private Stage mainStage = Main.rootStage;

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        close.setOnAction(e -> mainStage.close());
        minimize.setOnAction(e -> mainStage.setIconified(true));

        title.setStyle("-fx-font-family: LOBSTER; -fx-font-size: 20");
        icon.setStyle("-fx-font-family: Ubuntu; -fx-font-size: 25");

        headerBar.setCursor(Cursor.HAND);
        headerBar.setOnMousePressed(event -> {
            xOffset = mainStage.getX() - event.getScreenX();
            yOffset = mainStage.getY() - event.getScreenY();
        });
        headerBar.setOnMouseDragged(event -> {
            mainStage.setX(event.getScreenX() + xOffset);
            mainStage.setY(event.getScreenY() + yOffset);
        });
        icon.setOnMouseEntered(e -> new Shake(icon).play());
    }
}
