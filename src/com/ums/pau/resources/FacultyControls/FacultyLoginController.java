package com.ums.pau.resources.FacultyControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.ums.pau.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FacultyLoginController implements Initializable {
    @FXML
    public JFXTextField userName;
    @FXML
    public JFXPasswordField passWord;
    @FXML
    public javafx.scene.control.Label error;
    @FXML
    public Hyperlink forgotP;
    public JFXButton login;

    @FXML
    public void checkLogin() throws IOException {
        //TODO: add verification
        if (userName.getText().equals("r") || passWord.getText().equals("toor")) {
            new SceneSwitcher().switchSceneTo("resources/FacultyControls/facultyDashboard.fxml");
        } else error.setVisible(true);
    }

    public void forgotPass(ActionEvent event) {
        event.getEventType();
        error.setVisible(true);
        forgotP.setVisible(false);
    }

    public void backToHome() {
        try {
            new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setDefaultButton(true);
    }
}
