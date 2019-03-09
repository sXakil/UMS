package com.ums.pau.resources.AdminControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.ums.pau.SceneSwitcher;
import com.ums.pau.Shake;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    @FXML
    private JFXTextField adminID;
    @FXML
    private JFXPasswordField adminPass;
    @FXML
    private Label wrongPass;
    @FXML
    private Label wrongID;
    @FXML
    private Label wrongIDFormat;
    @FXML
    private JFXButton login;

    public void backToHome() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }

    public void validate() throws IOException {
        wrongIDFormat.setVisible(false);
        wrongID.setVisible(false);
        wrongPass.setVisible(false);
        //TODO: Add admins
        if (adminID.getText().equals("root") || adminPass.getText().equals("toor")) {
            new SceneSwitcher().switchSceneTo("resources/AdminControls/adminDashboard.fxml");
        } else {
            new Shake(login).play();
            wrongID.setVisible(true);
            wrongPass.setVisible(true);
            adminID.clear();
            adminPass.clear();
        }
    }

    public void validateEmail() {
        wrongID.setVisible(false);
        wrongPass.setVisible(false);
        if (adminID.getText().length() > 3 &&
                !adminID.getText().substring(0, 3).equals("__@")) {
            wrongIDFormat.setVisible(true);
        } else {
            wrongIDFormat.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setDefaultButton(true);
        login.disableProperty().bind(
                Bindings.isEmpty(adminID.textProperty())
                        .or(Bindings.isEmpty(adminPass.textProperty()))
        );
    }
}
