package com.ums.pau.resources.AdminControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    public JFXTextField adminID;
    public JFXPasswordField adminPass;
    public Label wrongPass;
    public Label wrongID;
    public Label wrongIDFormat;
    public JFXButton login;

    public void backToHome() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }

    public void validate() throws IOException {
        //TODO: Add admins
        if(adminID.getText().equals("a") || adminPass.getText().equals("adminPassword")) {
            new SceneSwitcher().switchSceneTo("resources/AdminControls/adminDashboard.fxml");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setDefaultButton(true);
    }
}
