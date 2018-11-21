package com.ums.pau;

import com.jfoenix.controls.JFXPasswordField;
import com.mongodb.*;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDashboardController implements Initializable {
    public Pane home, gradeReport, changePass;
    public VBox vBox;
    public JFXPasswordField oldPass, newPass, confirmPass;
    public Label passSuccess, invalidPass, wrongPass, passMatch;
    private String id = StudentLoginController.id;
    public Label studentName;

    private DBCollection dataRetriever(String collName) {
        return getCollection(collName);
    }

    static DBCollection getCollection(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("UMS");
        return db.getCollection(collName);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toHome();
        DBCollection collection = dataRetriever("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        studentName.setText(object.get("name") == null ? "Unknown" : object.get("name").toString());
        collection = dataRetriever("results");
        cursor = collection.find(query);
        query = new BasicDBObject("semester", 1);
        getReport(cursor.sort(query));
    }

    private void getReport(DBCursor cursor) {
        int i = 1;
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            if (object.get("semester").toString().contains(String.valueOf(i))) {
                HBox hBox = new HBox();
                hBox.setId("hBox-header");
                Label semester = new Label("Semester");
                semester.setId("itemH");
                Label course = new Label("Course Code");
                course.setId("itemH");
                Label credit = new Label("Credit");
                credit.setId("itemH");
                Label mark = new Label("Mark");
                mark.setId("itemH");
                Label cgpa = new Label("Grade");
                cgpa.setId("itemH");
                hBox.getChildren().addAll(semester, course, credit, mark, cgpa);
                if (i > 1) VBox.setMargin(hBox, new Insets(20, 0, 0, 0));
                vBox.getChildren().add(hBox);
                i++;
            }
            HBox hBox = new HBox();
            hBox.setId("hBox-list");
            Label semester = new Label(object.get("semester").toString());
            semester.setId("itemS");

            Label course = new Label(object.get("course_code").toString());
            course.setId("itemS");

            Label mark = new Label(object.get("mark").toString());
            mark.setId("itemS");

            Label credit = new Label(object.get("course_credit").toString());
            credit.setId("itemS");

            Label cgpa = new Label(getGrade(object.get("mark").toString()));
            cgpa.setId("itemS");

            hBox.getChildren().addAll(semester, course, credit, mark, cgpa);
            vBox.getChildren().add(hBox);
        }
    }


    private String getGrade(String mark) {
        double m = Double.parseDouble(mark);
        return markToCGPA(m);
    }

    static String markToCGPA(double m) {
        if (m >= 80) return "4.00";
        else if (m >= 75) return "3.75";
        else if (m >= 70) return "3.50";
        else if (m >= 65) return "3.25";
        else if (m >= 60) return "3.00";
        else if (m >= 55) return "2.75";
        else if (m >= 50) return "2.50";
        else if (m >= 45) return "2.25";
        else if (m >= 40) return "2.00";
        else return "0.00";
    }


    public void toHome() {
        gradeReport.setVisible(false);
        changePass.setVisible(false);
        home.setVisible(true);
    }

    public void toGrade() {
        home.setVisible(false);
        changePass.setVisible(false);
        gradeReport.setVisible(true);
    }

    public void toCPass() {
        home.setVisible(false);
        gradeReport.setVisible(false);
        changePass.setVisible(true);

    }

    public void toSupport() {

    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/landing.fxml");
    }

    public void validatePassword() {

    }
}