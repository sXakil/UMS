package com.ums.pau.resources.AdminControls.Dashboard;

import com.mongodb.DBCollection;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class DashboardController implements Initializable {
    static Stage facList;
    public Label totalFac, totalStud;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection tableS = getFrom( "students");
        totalStud.setText(String.valueOf(tableS.getCount()));
        DBCollection tableT = getFrom("teachers");
        totalFac.setText(String.valueOf(tableT.getCount()));
    }


    public void showFacList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("allFaculties.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Faculties");
        facList = stage;
        stage.show();
    }
    static Stage studList;
    public void showStudList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("allStudents.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Students");
        stage.setResizable(false);
        studList = stage;
        stage.show();
    }
}
