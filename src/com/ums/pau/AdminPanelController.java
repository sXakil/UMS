package com.ums.pau;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;

import java.io.IOException;

public class AdminPanelController {
    public JFXTextField adminID;
    public JFXPasswordField adminPass;
    public Label wrongPass;
    public Label wrongID;
    public Label wrongIDFormat;

    public void backToHome() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/landing.fxml");
    }

    public void validate() throws IOException {
        //TODO: Add admins
        if(adminID.getText().equals("a") || adminPass.getText().equals("adminPassword")) {
            new SceneSwitcher().switchSceneTo("resources/adminDashboard.fxml");
        }
        else {
            wrongID.setVisible(true);
            wrongPass.setVisible(true);
            adminID.clear();
            adminPass.clear();
        }
    }

    public void validateEmail() {
        wrongID.setVisible(false);
        wrongPass.setVisible(false);
        if(adminID.getText().length() > 3 &&
                !adminID.getText().substring(0, 3).equals("__@")) {
            wrongIDFormat.setVisible(true);
        } else {
            wrongIDFormat.setVisible(false);
        }
    }
}
