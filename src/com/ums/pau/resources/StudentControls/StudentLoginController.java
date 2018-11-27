package com.ums.pau.resources.StudentControls;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.*;
import com.ums.pau.BCrypt;
import com.ums.pau.SceneSwitcher;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
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
    private Hyperlink forgotP;
    @FXML
    private JFXButton login;

    static String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setDefaultButton(true);
        login.disableProperty().bind(
                Bindings.isEmpty(studentID.textProperty())
                        .or(Bindings.isEmpty(passWord.textProperty()))
        );
    }

    @FXML
    private void checkLogin() throws IOException {
        boolean studentExists = false;
        String givenPass = null;
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", studentID.getText());
        DBCursor results = collection.find(query);
        try {
            DBObject object = results.next();
            studentExists = true;
            givenPass = object.get("password").toString();
        } catch (Exception ne) {
            System.out.println("Empty input!");
        }
        if (studentExists && BCrypt.checkPassword(passWord.getText(), givenPass)) {
            id = studentID.getText();
            new SceneSwitcher().switchSceneTo("resources/StudentControls/studentDashboard.fxml");
        } else error.setVisible(true);
    }

    @FXML
    private void forgotPass() {
        error.setVisible(true);
        forgotP.setVisible(false);
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
