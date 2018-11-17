package com.ums.pau;

import com.jfoenix.controls.*;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDashboardController implements Initializable {
    @FXML
    public JFXTextField newStudName, newStudID, newStudDept, newStudSes, newStudPass;
    public JFXDatePicker newStudAdDate;
    public JFXButton addNewStud;
    public Label nSm, nSn, nSi, nSd, nSa, nSs;
    public Pane dash;
    public Pane mAddStud;
    public Label totalFac, totalStud;
    public JFXCheckBox male, female;
    public Pane preview;
    public Pane mAddTeacher;
    public JFXTextField newTeacherName;
    public JFXButton addNewTeacher;
    public JFXTextField newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass;
    public JFXDatePicker neTeacherJD;
    public Pane previewT;
    public Label nTn, nTp, nTm, nTd, nTj;
    public JFXCheckBox maleT, femaleT;

    private static boolean isValid = false;
    private static boolean addNew = true;
    private static boolean addNewT = true;

    private MongoCollection<Document> initMongo(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase db = mongoClient.getDatabase("UMS");
        return db.getCollection(collName);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toDashboardPane();
    }

    public void addNewStudent() {
        if(addNew) {
            MongoCollection<Document> table = initMongo("students");
            BasicDBObject document = new BasicDBObject("name", newStudName.getText())
                    .append("id", newStudID.getText())
                    .append("dept", newStudDept.getText().toUpperCase())
                    .append("session", newStudSes.getText())
                    .append("admissionDate", newStudAdDate.getValue() == null ? new Date() : newStudAdDate.getValue().toString())
                    .append("password", newStudPass.getText())
                    .append("gender", male.isSelected() ? "Male" : "Female")
                    .append("added_on", new Date());
            Document doc = new Document(document);
            table.insertOne(doc);
            preview.setVisible(true);
            nSn.setText(nSn.getText() + newStudName.getText());
            nSi.setText(nSi.getText() + newStudID.getText());
            nSd.setText(nSd.getText() + newStudDept.getText());
            nSa.setText(nSa.getText() + (newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString()));
            nSs.setText((nSs.getText() + newStudSes.getText()));
            newStudName.setDisable(true);
            newStudID.setDisable(true);
            newStudDept.setDisable(true);
            newStudSes.setDisable(true);
            newStudAdDate.setDisable(true);
            newStudPass.setDisable(true);
            addNewStud.setText("Add Another");
            addNew = false;
        } else {
            nSn.setText("Name: ");
            nSi.setText("ID: ");
            nSd.setText("Department: ");
            nSa.setText("Admission Date: ");
            nSs.setText("Session: ");
            preview.setVisible(false);
            newStudName.setDisable(false);
            newStudID.setDisable(false);
            newStudDept.setDisable(false);
            newStudSes.setDisable(false);
            newStudAdDate.setDisable(false);
            newStudPass.setDisable(false);
            newStudName.clear();
            newStudID.clear();
            newStudDept.clear();
            newStudSes.clear();
            newStudAdDate.setValue(null);
            newStudPass.clear();
            addNewStud.setText("Add");
            addNewStud.setDisable(true);
            addNew = true;
        }
    }



    public void addNewTeacher() {
        if(addNewT) {
            MongoCollection<Document> table = initMongo("teacher");
            BasicDBObject document = new BasicDBObject("name", newTeacherName.getText())
                    .append("position", newTeacherPosition.getText())
                    .append("major", newTeacherMajor.getText())
                    .append("department", newTeacherDept.getText())
                    .append("joiningDate", neTeacherJD.getValue() == null ? new Date() : neTeacherJD.getValue().toString())
                    .append("password", newTeacherPass.getText())
                    .append("gender", male.isSelected() ? "Male" : "Female")
                    .append("added_on", new Date());
            Document doc = new Document(document);
            table.insertOne(doc);
            previewT.setVisible(true);
            nTn.setText(nTn.getText() + newTeacherName.getText());
            nTp.setText(nTp.getText() + newTeacherPosition.getText());
            nTm.setText(nTm.getText() + newTeacherMajor.getText());
            nTd.setText((nTd.getText() + newTeacherDept.getText()));
            nTj.setText(nTj.getText() + (neTeacherJD.getValue() == null ? new Date().toString() : neTeacherJD.getValue().toString()));
            newTeacherName.setDisable(true);
            newTeacherPosition.setDisable(true);
            newTeacherMajor.setDisable(true);
            newTeacherDept.setDisable(true);
            neTeacherJD.setDisable(true);
            newTeacherPass.setDisable(true);
            addNewTeacher.setText("Add Another");
            addNewT = false;
        } else {
            nTn.setText("Name: ");
            nTp.setText("Position: ");
            nTm.setText("Major: ");
            nTd.setText("Department: ");
            nTj.setText("Joining Date: ");
            previewT.setVisible(false);
            newTeacherName.setDisable(false);
            newTeacherPosition.setDisable(false);
            newTeacherMajor.setDisable(false);
            newTeacherDept.setDisable(false);
            neTeacherJD.setDisable(false);
            newTeacherPass.setDisable(false);
            newTeacherName.clear();
            newTeacherPosition.clear();
            newTeacherMajor.clear();
            newTeacherDept.clear();
            neTeacherJD.setValue(null);
            newTeacherPass.clear();
            addNewTeacher.setText("Add");
            addNewTeacher.setDisable(true);
            addNewT = true;
        }
    }

    private void TextFieldChangedListener(JFXTextField[] textFields, JFXButton button) {
        for (JFXTextField fields : textFields) {
            if(fields.getText() == null || fields.getText().length() == 0) {
                isValid = false;
                break;
            } else {
                isValid = true;
            }
        }
        if(isValid) button.setDisable(false);
        else button.setDisable(true);
    }


    public void studentTextListener() {
        JFXTextField[] studentTextFields = {newStudName, newStudID, newStudDept, newStudSes, newStudPass};
        TextFieldChangedListener(studentTextFields, addNewStud);
    }
    public void teacherTextListener() {
        JFXTextField[] teacherTextFields = {newTeacherName, newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass};
        TextFieldChangedListener(teacherTextFields, addNewTeacher);
    }

    public void toAddStudentPane() {
        isValid = false;
        mAddTeacher.setVisible(false);
        dash.setVisible(false);
        mAddStud.setVisible(true);
    }
    public void toAddTeacherPane() {
        isValid = false;
        dash.setVisible(false);
        mAddStud.setVisible(false);
        mAddTeacher.setVisible(true);
    }
    public void toDashboardPane() {
        MongoCollection<Document> tableS = initMongo( "students");
        totalStud.setText(String.valueOf(tableS.countDocuments()));
        MongoCollection<Document> tableT = initMongo( "teacher");
        totalFac.setText(String.valueOf(tableT.countDocuments()));
        mAddTeacher.setVisible(false);
        mAddStud.setVisible(false);
        dash.setVisible(true);
    }

    public void genPass() {
        GenPass gp = new GenPass();
        newStudPass.setText(gp.getString());
        studentTextListener();
    }

    public void genTPass() {
        GenPass gp = new GenPass();
        newTeacherPass.setText(gp.getString());
        teacherTextListener();
    }

    public void isMale() {
        if(!male.isSelected())
            female.setSelected(true);
        else
            female.setSelected(false);
    }
    public void isFemale() {
        if(!female.isSelected())
            male.setSelected(true);
        else
            male.setSelected(false);
    }

    public void isMaleT() {
        if(!maleT.isSelected())
            femaleT.setSelected(true);
        else
            femaleT.setSelected(false);
    }
    public void isFemaleT() {
        if(!femaleT.isSelected())
            maleT.setSelected(true);
        else
            maleT.setSelected(false);
    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/landing.fxml");
    }
}