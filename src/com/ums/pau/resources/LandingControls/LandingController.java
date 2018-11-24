package com.ums.pau.resources.LandingControls;

import com.ums.pau.SceneSwitcher;

import java.io.IOException;

public class LandingController {
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
