package com.ums.pau.resources.StudentControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.*;
import com.ums.pau.BCrypt;
import com.ums.pau.SceneSwitcher;
import com.ums.pau.Shake;
import com.ums.pau.resources.ForgottenPasswordPrompt;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class StudentLoginController implements Initializable {
    @FXML
    private JFXTextField studentID;
    @FXML
    private JFXPasswordField passWord;
    @FXML
    private javafx.scene.control.Label error;
    @FXML
    private JFXButton login;

    public static String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setDefaultButton(true);
        login.disableProperty().bind(
                Bindings.isEmpty(studentID.textProperty())
                        .or(Bindings.isEmpty(passWord.textProperty()))
        );
    }

    @FXML
    private void checkLogin() {
        error.setVisible(false);
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", studentID.getText());
        DBCursor results = collection.find(query);
        try {
            DBObject object = results.next();
            String passInDB = object.get("password").toString();
            if (BCrypt.checkPassword(passWord.getText(), passInDB)) {
                id = studentID.getText();
                new SceneSwitcher().switchSceneTo("resources/StudentControls/studentDashboard.fxml");
            } else {
                error.setVisible(true);
                studentID.clear();
                passWord.clear();
            }
        } catch (Exception ne) {
            error.setVisible(true);
            new Shake(login).play();
        }
    }

    @FXML
    private void forgotPass() throws IOException {
        new ForgottenPasswordPrompt().show();
    }

    @FXML
    private void backToHome() {
        try {
            new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
