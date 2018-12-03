package com.ums.pau.resources.StudentControls;

import com.mongodb.*;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class StudentDashboardController implements Initializable {

    public StackPane stackPane;
    private String id = StudentLoginController.id;
    public Label studentName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            toHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        studentName.setText(object.get("name").toString());
    }



    public void toHome() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("DashBoard/dashboard.fxml")));
    }

    public void toGrade() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("GradeReport/gradeReport.fxml")));
    }

    public void toCPass() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("ChangePassword/changePassword.fxml")));

    }

//    public void toSupport() throws IOException {
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("Support/support.fxml")));
//    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }
}
