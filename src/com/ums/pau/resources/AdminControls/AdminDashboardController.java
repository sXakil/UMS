package com.ums.pau.resources.AdminControls;

import com.ums.pau.SceneSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    public StackPane stackPane;
    public static boolean modStudent = false;
    public static boolean modFaculty = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            toDashboardPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toDashboardPane() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("Dashboard/dashboard.fxml")));
    }

    public void toAddStudentPane() throws IOException {
        modStudent = false;
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("UpsertStudent/addStudent.fxml")));
    }

    public void toModifyStudent() throws IOException {
        modStudent = true;
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("UpsertStudent/addStudent.fxml")));
    }

    public void toDeleteStudent() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("DeleteStudent/deletePane.fxml")));
    }

    public void toAddTeacherPane() throws IOException {
        modFaculty = false;
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("UpsertFaculty/addFaculty.fxml")));
    }

    public void toModifyFac() throws IOException {
        modFaculty = true;
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("UpsertFaculty/addFaculty.fxml")));
    }

    public void toDeleteFac() throws IOException {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(FXMLLoader.load(getClass().getResource("DeleteFaculty/deletePane.fxml")));
    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }
}
