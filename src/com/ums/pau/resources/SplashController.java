package com.ums.pau.resources;

import com.ums.pau.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {
        public void run() {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    try {
                        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
