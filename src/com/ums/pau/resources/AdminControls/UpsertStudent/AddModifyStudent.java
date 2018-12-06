package com.ums.pau.resources.AdminControls.UpsertStudent;

import com.jfoenix.controls.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.ums.pau.BCrypt;
import com.ums.pau.resources.AdminControls.AdminDashboardController;
import com.ums.pau.resources.AdminControls.UpsertFaculty.AddModifyFaculty;
import com.ums.pau.resources.GenPass;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.bson.Document;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;
import static com.ums.pau.DatabaseHandler.insertInto;

public class AddModifyStudent implements Initializable {

    public JFXTextField newStudName, newStudID, newStudDept, newStudPass, searchStud;
    public JFXDatePicker newStudAdDate;
    public JFXComboBox<String> newStudSes;
    public Label notification, nameLabel, idLabel, deptLabel, adDateLabel, sessionLabel, addStudTitle, zeroSearchResult;
    public JFXCheckBox male, female;
    public JFXButton addNewStud;
    public Pane preview, modifyStudent;

    private static boolean addNew = true;
    private static String altBtnText;

    public void addNewStudent() {
        if (addNew) {
            MongoCollection<Document> collection = insertInto("students");
            collection.updateOne(
                    Filters.eq("id", newStudID.getText()), new Document("$set",
                            new Document()
                                    .append("name", newStudName.getText())
                                    .append("id", newStudID.getText())
                                    .append("dept", newStudDept.getText().toUpperCase())
                                    .append("session", newStudSes.getSelectionModel().getSelectedItem())
                                    .append("gender", male.isSelected() ? "Male" : "Female")
                                    .append("admissionDate", newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString())
                                    .append("password", BCrypt.hashPassword(newStudPass.getText(), BCrypt.genSalt()))
                                    .append("added_on", new Date())),
                    new UpdateOptions().upsert(true));
            if (isDuplicate()) {
                notification.setText("An existing student was updated!");
                notification.setTextFill(Color.CORAL);
            }
            preview.setVisible(true);
            nameLabel.setText("Name: " + newStudName.getText());
            idLabel.setText("ID: " + newStudID.getText());
            deptLabel.setText("Department: " + newStudDept.getText());
            adDateLabel.setText("Admission Date: " + (newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString()));
            sessionLabel.setText(("Session: " + newStudSes.getSelectionModel().getSelectedItem()));
            disableStudentsFields(true);
            addNewStud.setText(altBtnText);
            addNew = false;
        } else {
            preview.setVisible(false);
            clearStudentFields();
            disableStudentsFields(false);
            addNewStud.setText("Add");
            addNew = true;
        }
    }

    private void clearStudentFields() {
        newStudName.clear();
        newStudID.clear();
        newStudDept.clear();
        newStudSes.getSelectionModel().clearSelection();
        newStudAdDate.setValue(null);
        newStudPass.clear();
    }

    private void disableStudentsFields(boolean disable) {
        if (disable) {
            newStudName.setDisable(true);
            newStudID.setDisable(true);
            newStudDept.setDisable(true);
            newStudSes.setDisable(true);
            newStudAdDate.setDisable(true);
            newStudPass.setDisable(true);
        } else {
            newStudName.setDisable(false);
            newStudID.setDisable(false);
            newStudDept.setDisable(false);
            newStudSes.setDisable(false);
            newStudAdDate.setDisable(false);
            newStudPass.setDisable(false);
        }

    }

    private boolean isDuplicate() {
        BasicDBObject bd = new BasicDBObject("id", newStudID.getText());
        DBCollection dc = getFrom("students");
        DBCursor cursor = dc.find(bd);
        try {
            DBObject dbo = cursor.next();
            return !Objects.equals(dbo.get("id").toString(), newStudID.getText());
        } catch (NoSuchElementException e) {
            return true;
        }
    }


    public void searchStudent() {
        zeroSearchResult.setVisible(false);
        boolean checked = false;
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", searchStud.getText());
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            newStudID.setText(object.get("id").toString());
            newStudName.setText(object.get("name").toString());
            newStudDept.setText(object.get("dept").toString());
            String s = object.get("session").toString();
            newStudSes.getSelectionModel().select(s.equals("Spring") ? 0 : s.equals("Summer") ? 1 : 2);
            newStudAdDate.setValue(LocalDate.parse(object.get("admissionDate").toString()));
            getGender(object.get("gender").toString(), male, female);
            checked = true;
        }
        if (!checked) zeroSearchResult.setVisible(true);
    }

    private void getGender(String string, JFXCheckBox maleT, JFXCheckBox femaleT) {
        AddModifyFaculty.toGender(string, maleT, femaleT);
    }

    public void isMale() {
        if (!male.isSelected())
            female.setSelected(true);
        else
            female.setSelected(false);
    }

    public void isFemale() {
        if (!female.isSelected())
            male.setSelected(true);
        else
            male.setSelected(false);
    }

    public void genPass() {
        GenPass gp = new GenPass();
        newStudPass.setText(gp.getString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newStudSes.getItems().addAll("Spring", "Summer", "Fall");
        BooleanProperty b = new SimpleBooleanProperty(AdminDashboardController.modStudent);
        modifyStudent.visibleProperty().bind(b);
        if (modifyStudent.isVisible()) {
            addNewStud.setText("Modify");
            notification.setText("An existing student was updated!");
            notification.setTextFill(Color.CORAL);
            altBtnText = "Modify Another";
        } else {
            addNewStud.setText("Add");
            notification.setText("New student added successfully!");
            notification.setTextFill(Color.GREEN);
            altBtnText = "Add Another";
        }
        if (modifyStudent.isVisible()) addStudTitle.setText("Modify a Student");
        else addStudTitle.setText("Add a new Student");
        addNewStud.disableProperty().bind(
                Bindings.isEmpty(newStudID.textProperty())
                        .or(Bindings.isEmpty(newStudName.textProperty()))
                        .or(Bindings.isEmpty(newStudDept.textProperty()))
                        .or(Bindings.isNull(newStudAdDate.getEditor().textProperty()))
                        .or(Bindings.isEmpty(newStudPass.textProperty()))
        );
    }
}
