package com.ums.pau;

import com.mongodb.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDashboardController implements Initializable {
    public Pane home, gradeReport, changePass;
    public VBox vBox;
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
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            HBox hBox = new HBox();
            Label sem = new Label(object.get("semester").toString());
            sem.setId("item");
            Label course = new Label(object.get("course_code").toString());
            course.setId("item");
            Label mark = new Label(object.get("mark").toString());
            mark.setId("item");
            Label credit = new Label(object.get("course_credit").toString());
            credit.setId("item");
            hBox.getChildren().addAll(sem, course, mark, credit);
            vBox.getChildren().add(hBox);
        }
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
}