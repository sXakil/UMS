package com.ums.pau.resources;

import com.ums.pau.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class SplashController implements Initializable {

    public Label loader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new SplashScreen().start();
    }
    class SplashScreen extends Thread {
        public void run() {
            try {
                for(int i = 1; i <= 100; i++) {
                    final int loaded = i;
                    Thread.sleep(ThreadLocalRandom.current().nextInt(30, 70));
                    Platform.runLater(() -> loader.setText(loaded + "%"));
                }
                Thread.sleep(200);
                Platform.runLater(() -> {
                    try { new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml"); }
                    catch (IOException e) { e.printStackTrace(); }
                });
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
