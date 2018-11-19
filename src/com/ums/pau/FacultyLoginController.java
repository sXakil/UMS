package com.ums.pau;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import java.io.IOException;

public class FacultyLoginController {
    @FXML
    public JFXTextField userName;
    @FXML
    public JFXPasswordField passWord;
    @FXML
    public javafx.scene.control.Label error;
    @FXML
    public Hyperlink forgotP;

    @FXML
    public void checkLogin() throws IOException {
        //TODO: add verification
        if (userName.getText().equals("r") || passWord.getText().equals("toor")) {
            new SceneSwitcher().switchSceneTo("resources/facultyDashboard.fxml");
        } else error.setVisible(true);
    }

    public void forgotPass(ActionEvent event) {
        event.getEventType();
        error.setVisible(true);
        forgotP.setVisible(false);
    }

    public void backToHome() {
        try {
            new SceneSwitcher().switchSceneTo("resources/landing.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}