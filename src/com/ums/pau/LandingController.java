package com.ums.pau;

import java.io.IOException;

public class LandingController {
    public void studentLogin() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/studentLogin.fxml");
    }
    public void adminLogin() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/facultyLogin.fxml");
    }
    public void adminPanel() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/adminPanel.fxml");
    }
}
