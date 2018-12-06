package com.ums.pau.resources.LandingControls;

import animatefx.animation.Jello;
import com.jfoenix.controls.JFXButton;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LandingController implements Initializable {

    public JFXButton toAdmin, toStudent, toFaculty;
    public ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toAdmin.setCursor(Cursor.HAND);
        toFaculty.setCursor(Cursor.HAND);
        toStudent.setCursor(Cursor.HAND);

        logo.setOnMouseEntered(e -> new Jello(logo).play());
    }

    public void studentLogin() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/StudentControls/studentLogin.fxml");
    }

    public void adminLogin() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/FacultyControls/facultyLogin.fxml");
    }

    public void adminPanel() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/AdminControls/adminPanel.fxml");
    }
}