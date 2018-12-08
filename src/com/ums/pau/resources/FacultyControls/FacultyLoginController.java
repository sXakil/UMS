package com.ums.pau.resources.FacultyControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.ums.pau.BCrypt;
import com.ums.pau.SceneSwitcher;
import com.ums.pau.Shake;
import com.ums.pau.resources.ForgottenPasswordPrompt;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class FacultyLoginController implements Initializable {
    @FXML
    private JFXTextField teacherID;
    @FXML
    private JFXPasswordField passWord;
    @FXML
    private Label error;
    @FXML
    private JFXButton login;

    static String facName = null;

    @FXML
    private void checkLogin() throws IOException {
        error.setVisible(false);
        boolean facultyExists = false;
        String passInDB = null;
        DBCollection collection = getFrom("teachers");
        BasicDBObject query = new BasicDBObject("uNID", teacherID.getText());
        DBCursor results = collection.find(query);
        try {
            DBObject object = results.next();
            facultyExists = true;
            passInDB = object.get("password").toString();
            facName = object.get("name").toString();
        } catch (Exception ne) {
            error.setVisible(true);
        }
        if (facultyExists && BCrypt.checkPassword(passWord.getText(), passInDB)) {
            new SceneSwitcher().switchSceneTo("resources/FacultyControls/facultyDashboard.fxml");
        } else {
            new Shake(login).play();
            error.setVisible(true);
            teacherID.clear();
            passWord.clear();
        }
    }

    public void forgotPass() throws IOException {
        new ForgottenPasswordPrompt().show();
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
        login.disableProperty().bind(
                Bindings.isEmpty(teacherID.textProperty())
                        .or(Bindings.isEmpty(passWord.textProperty()))
        );
    }
}
