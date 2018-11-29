package com.ums.pau.resources.FacultyControls;

import com.jfoenix.controls.*;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.bson.Document;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static com.ums.pau.DatabaseHandler.*;


public class FacultyDashboardController implements Initializable {

    public VBox vBox;
    public Label nameLab;
    public JFXTextField searchToEdit;
    public JFXComboBox<String> semCB;
    public Pane prevPane;
    public JFXTextField idTF;
    public JFXTextField courseTF;
    public JFXButton resultBTN;
    public VBox resultVBox;
    public JFXCheckBox c3, c1;
    private static boolean isValid = false;
    public Label failed, success;
    public JFXTextField markTF;
    public JFXTextArea commentTF;
    public Label facName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        failed.setVisible(false);
        success.setVisible(false);
        isValid = false;
        resultBTN.setDisable(true);
        facName.setText("Hello, " + FacultyLoginController.facName);
        semCB.getItems().setAll("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th");
        DBCollection collection = getFrom("students");
        DBCursor students = collection.find();
        BasicDBObject sort = new BasicDBObject("id", 1);
        students.sort(sort);
        setNewHBox(students);
    }

    private void editorFill(String id) {
        isValid = false;
        DBCollection collection = getFrom("students");
        BasicDBObject dbObject = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(dbObject);
        DBObject object = cursor.next();
        prevPane.setVisible(true);
        nameLab.setText(object.get("name").toString());
        idTF.setText(object.get("id").toString());
        idTF.setDisable(true);
        collection = getFrom("results");
        cursor = collection.find(dbObject);
        BasicDBObject sorter = new BasicDBObject("semester", 1);
        cursor.sort(sorter);
        resultVBox.getChildren().clear();
        Label l = new Label("Previous Records of " + object.get("name").toString());
        l.setStyle("-fx-min-width: inherit; -fx-font-size: 18px; -fx-alignment: center;");
        resultVBox.getChildren().add(l);
        int i = 1;
        while (cursor.hasNext()) {
            object = cursor.next();
            if (object.get("semester").toString().contains(String.valueOf(i))) {
                HBox hBox = new HBox();
                hBox.setId("hBox-header");
                Label semester = new Label(i == 1 ? "1st Semester" : i == 2 ? "2nd Semester" : i == 3 ? "3rd Semester" : i + "th Semester");
                semester.setId("itemH");
                hBox.getChildren().add(semester);
                hBox.setStyle("-fx-alignment: center");
                Pane p = new Pane();
                p.setStyle("-fx-pref-height: 1px; -fx-background-color: darkgray");
                if (i > 1)
                    resultVBox.getChildren().add(p);
                VBox.setMargin(hBox, new Insets(25, 0, 0, 0));
                resultVBox.getChildren().add(hBox);
                i++;
            }
            HBox hBox = new HBox();
            hBox.setMinWidth(500);
            Label courseName = new Label(object.get("course_code").toString());
            courseName.setStyle("-fx-alignment: center; -fx-min-width: 245");

            Label grade = new Label(object.get("mark").toString());
            grade.setStyle("-fx-alignment: center; -fx-min-width: 245");

            hBox.getChildren().addAll(courseName, grade);
            hBox.setStyle("-fx-alignment: center");

            hBox.setPadding(new Insets(5));
            hBox.setStyle("-fx-border-color: darkgray; -fx-border: 0 0 0 1px");
            resultVBox.getChildren().add(hBox);
        }
    }

    public void findToEdit() {
        vBox.getChildren().clear();
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", searchToEdit.getText());
        DBCursor students = collection.find(query);
        setNewHBox(students);
        if (searchToEdit.getText().length() < 1) {
            students = collection.find();
            query = new BasicDBObject("id", 1);
            students.sort(query);
            setNewHBox(students);
        }
    }

    private void setNewHBox(DBCursor students) {
        System.out.println(students.count());
        if (students.count() < 1) {
            vBox.getChildren().add(new Label("No records found!"));
            vBox.setStyle("-fx-alignment: center");
        } else {
            vBox.getChildren().clear();
        }
        while (students.hasNext()) {
            DBObject object = students.next();
            String id = object.get("id").toString();
            String name = object.get("name").toString();
            String dept = object.get("dept").toString();
            String gender = object.get("gender").toString();

            HBox hb = new HBox();
            hb.setId("hBox-list");
            vBox.getChildren().add(hb);

            Label lb1 = new Label(name);
            lb1.setId("nameItem");
            hb.getChildren().add(lb1);

            Label lb2 = new Label(id);
            lb2.setId("item");
            hb.getChildren().add(lb2);

            Label lb3 = new Label(dept);
            lb3.setId("item");
            hb.getChildren().add(lb3);

            Label lb4 = new Label(gender);
            lb4.setId("item");
            hb.getChildren().add(lb4);

            Pane sep = new Pane();
            sep.setPrefHeight(1.0);
            sep.setPrefWidth(596.0);
            sep.setId("sep-pane");
            vBox.getChildren().add(sep);

            hb.setOnMouseClicked(t -> editorFill(id));
        }
    }

    public void enterResult() {
        resultBTN.setText("Enter");
        DBCollection collection = getFrom("results");
        BasicDBObject query = new BasicDBObject("course_code", courseTF.getText().toUpperCase().replaceAll("[ \\-]", ""));
        DBCursor cursor = collection.find(query);
        DBObject check = null;
        if (cursor.hasNext()) check = cursor.next();
        if (check != null && check.get("course_code").toString().equals(courseTF.getText())) {
            failed.setVisible(true);
            resultBTN.setText("Try Again");
            resultBTN.setDisable(false);
            System.out.println(check.get("course_code").toString() + " " + courseTF.getText());
        } else {
            MongoCollection<Document> grades = insertInto("results");
            BasicDBObject dbObject = new BasicDBObject("id", idTF.getText())
                    .append("semester", semCB.getSelectionModel().getSelectedItem())
                    .append("course_code", courseTF.getText().toUpperCase().replaceAll("[ \\-]", ""))
                    .append("mark", markTF.getText())
                    .append("course_credit", c3.isSelected() ? "3" : "1")
                    .append("comment", commentTF.getText() == null ? "" : commentTF.getText())
                    .append("added_by", FacultyLoginController.facName);
            Document doc = new Document(dbObject);
            grades.insertOne(doc);
            editorFill(idTF.getText());
            semCB.getSelectionModel().clearSelection();
            courseTF.clear();
            markTF.clear();
            resultBTN.setDisable(true);
            success.setVisible(true);
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
                markTF.getText().length() > 0;
        if (isValid) resultBTN.setDisable(false);
        else resultBTN.setDisable(true);
    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }
}