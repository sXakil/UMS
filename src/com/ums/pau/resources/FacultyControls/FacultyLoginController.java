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
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
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
    private Hyperlink forgotP;
    public JFXButton login;
    static String facName = null;

    @FXML
    private void checkLogin() throws IOException {
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
            ne.printStackTrace();
        }
        if (facultyExists && BCrypt.checkPassword(passWord.getText(), passInDB)) {
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
        login.disableProperty().bind(
                Bindings.isEmpty(teacherID.textProperty())
                        .or(Bindings.isEmpty(passWord.textProperty()))
        );
    }
}
