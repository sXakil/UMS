package com.ums.pau;

import com.jfoenix.controls.*;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDashboardController implements Initializable {
    public JFXTextField newStudName, newStudID, newStudDept, newStudSes, newStudPass;
    public JFXDatePicker newStudAdDate;
    public JFXButton addNewStud;
    public Label nSm, nSn, nSi, nSd, nSa, nSs;
    public Pane dash;
    public Pane mAddStud;
    public Label totalFac, totalStud;
    public JFXCheckBox male, female;
    private static boolean isValid = false;
    private static boolean addNew = true;
    public Pane preview;


    private MongoCollection<Document> initMongo(String dbName, String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase db = mongoClient.getDatabase(dbName);
        return db.getCollection(collName);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mToDash();
    }

    public void addNewStudent() {
        if(addNew) {
            MongoCollection<Document> table = initMongo("UMS", "students");
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

    public void onTextFieldChangedListener() {
        JFXTextField[] textFields = {newStudName, newStudID, newStudDept, newStudSes, newStudPass};
        for (JFXTextField fields : textFields) {
            if(fields.getText() == null || fields.getText().length() == 0) {
                isValid = false;
                break;
            } else {
                isValid = true;
            }
        }
        if(isValid) addNewStud.setDisable(false);
        else addNewStud.setDisable(true);
    }


    public void toAddPane() {
        dash.setVisible(false);
        mAddStud.setVisible(true);
    }

    public void mToDash() {
        MongoCollection<Document> table = initMongo("UMS", "students");
        totalStud.setText(String.valueOf(table.countDocuments()));
        mAddStud.setVisible(false);
        dash.setVisible(true);
    }

    public void genPass() {
        GenPass gp = new GenPass();
        newStudPass.setText(gp.getString());
        onTextFieldChangedListener();
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
}