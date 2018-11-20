package com.ums.pau;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.bson.Document;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FacultyDashboardController implements Initializable {

    public VBox vBox;
    public Label nameLab;
    public JFXTextField searchToEdit;
    public JFXComboBox<String> semCB, gpCB;
    public Pane prevPane;
    public JFXTextField idTF;
    public JFXTextField courseTF;
    public JFXButton resultBTN;
    public VBox resultVBox;
    public JFXCheckBox c3, c1;
    private static boolean isValid = false;
    public Label failed, success;

    private DBCollection dataRetriever(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("UMS");
        return db.getCollection(collName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isValid = false;
        resultBTN.setDisable(true);
        semCB.getItems().setAll("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th");
        gpCB.getItems().setAll("4.0", "3.75", "3.50", "3.25", "3.00", "2.75", "2.50", "2.25", "2.00", "0.00");
        DBCollection collection = dataRetriever("students");
        DBCursor students = collection.find();
        BasicDBObject sort = new BasicDBObject("id", 1);
        students.sort(sort);
        setNewHBox(students);
    }

    private void editorFill(String id) {
        getCGPA(id);
        isValid = false;
        DBCollection collection = dataRetriever("students");
        BasicDBObject dbObject = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(dbObject);
        DBObject object = cursor.next();
        prevPane.setVisible(true);
        nameLab.setText(object.get("name").toString());
        idTF.setText(object.get("id").toString());
        idTF.setDisable(true);

        collection = dataRetriever("results");
        cursor = collection.find(dbObject);
        BasicDBObject sorter = new BasicDBObject("semester", 1);
        cursor.sort(sorter);
        resultVBox.getChildren().clear();
        while (cursor.hasNext()) {
            object = cursor.next();
            HBox hBox = new HBox();
            resultVBox.getChildren().add(hBox);
            Label courseName = new Label(object.get("course_code").toString());
            Label s1 = new Label("    -   ");
            Label s2 = new Label("    -   ");
            Label grade = new Label(object.get("gp").toString());
            Label semester = new Label(object.get("semester").toString());
            hBox.getChildren().addAll(semester, s1, courseName, s2, grade);
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(20));
        }
    }

    private MongoCollection<Document> initMongo() {
        return getDocumentMongoCollection("results");
    }

    static MongoCollection<Document> getDocumentMongoCollection(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase db = mongoClient.getDatabase("UMS");
        return db.getCollection(collName);
    }

    public void findToEdit() {
        vBox.getChildren().clear();
        DBCollection collection = dataRetriever("students");
        BasicDBObject query = new BasicDBObject("id", searchToEdit.getText());
        DBCursor students = collection.find(query);
        setNewHBox(students);
        if (searchToEdit.getText().length() < 1) {
            students = collection.find();
            setNewHBox(students);
        }
    }

    private void setNewHBox(DBCursor students) {
        System.out.println(students.count());
        if (students.count() < 1) {
            vBox.getChildren().add(new Label("No records found!"));
            vBox.setAlignment(Pos.CENTER);
        } else {
            vBox.getChildren().clear();
        }
        while (students.hasNext()) {
            DBObject object = students.next();
            String id = object.get("id").toString();
            String name = object.get("name").toString();
            String dept = object.get("dept").toString();
            String gender = object.get("gender").toString();
            //System.out.println(id + " " + name + " " + dept + " " + gender);

            //node[i] = FXMLLoader.load(getClass().getResource("resources/studentsItem.fxml"));
            HBox hb = new HBox();
            hb.setId("hBox-list");
            //hb.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hb);

            Label lb1 = new Label(name);
            lb1.setId("item");
            //lb1.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb1);

            Label lb2 = new Label(id);
            lb2.setId("item");
            //lb2.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb2);

            Label lb3 = new Label(dept);
            lb3.setId("item");
            //lb3.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb3);
            //String cgp = getCGPA(id);
            Label lb4 = new Label(gender);
            lb4.setId("item");
            //lb4.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb4);

            hb.setOnMouseClicked(t -> editorFill(id));

        }
    }

    private void getCGPA(String id) {
        DBCollection collection = dataRetriever("results");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject doc;
        Double gp, cc;
        double totalGradePoint = 0, totalCredit = 0, gpMultiple = 0, gpDivisor = 0;
        while (cursor.hasNext()) {
            doc = cursor.next();
            gp = Double.parseDouble(doc.get("gp").toString());
            cc = Double.parseDouble(doc.get("course_credit").toString());
            totalGradePoint += gp;
            totalCredit += cc;
            if (cc == 3.00) {
                gpMultiple += gp * 4;
                gpDivisor += 4;
            } else if (cc == 1.00) {
                gpMultiple += gp;
                gpDivisor += 1;
            }
        }
        double cgp = gpMultiple / gpDivisor;
        DecimalFormat df = new DecimalFormat("#.##");
        cgp = cgp > 1.0 ? Double.valueOf(df.format(cgp)) : 0.0;
        System.out.println(totalGradePoint + " " + totalCredit + " " + cgp);
    }

    public void enterResult() {
        failed.setVisible(false);
        success.setVisible(false);
        resultBTN.setText("Enter");
        DBCollection collection = dataRetriever("results");
        BasicDBObject query = new BasicDBObject("course_code", courseTF.getText().toUpperCase().replaceAll("[ \\-]", ""));
        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            failed.setVisible(true);
            resultBTN.setText("Try Again");
            resultBTN.setDisable(false);
        } else {
            MongoCollection<Document> grades = initMongo();
            BasicDBObject dbObject = new BasicDBObject("id", idTF.getText())
                    .append("semester", semCB.getSelectionModel().getSelectedItem())
                    .append("course_code", courseTF.getText().toUpperCase().replaceAll("[ \\-]", ""))
                    .append("gp", gpCB.getSelectionModel().getSelectedItem())
                    .append("course_credit", c3.isSelected() ? "3" : "1");
            Document doc = new Document(dbObject);
            grades.insertOne(doc);
            success.setVisible(true);
            idTF.clear();
            semCB.getSelectionModel().clearSelection();
            courseTF.clear();
            gpCB.getSelectionModel().clearSelection();
            resultBTN.setDisable(true);
        }
    }


    public void isThree() {
        if (!c3.isSelected())
            c1.setSelected(true);
        else
            c1.setSelected(false);
    }

    public void isOne() {
        if (!c1.isSelected())
            c3.setSelected(true);
        else
            c3.setSelected(false);
    }

    public void filled() {
        failed.setVisible(false);
        success.setVisible(false);
        isValid = idTF.getText().length() > 0 &&
                courseTF.getText().length() > 5 &&
                semCB.getValue() != null &&
                gpCB.getValue() != null;
        if (isValid) resultBTN.setDisable(false);
        else resultBTN.setDisable(true);
    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/landing.fxml");
    }
}
//    @FXML
//    public void switchToLogin() throws IOException {
//        new SceneSwitcher().switchSceneTo("resources/studentLogin.fxml");
//    }
