package com.ums.pau.resources.AdminControls.UpsertStudent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import org.bson.conversions.Bson;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;
import static com.ums.pau.DatabaseHandler.insertInto;

public class AddModifyStudent implements Initializable {

    public JFXTextField newStudName, newStudID, newStudDept, newStudSes, newStudPass, searchStud;
    public JFXDatePicker newStudAdDate;
    public Label notification, nameLabel, idLabel, deptLabel, adDateLabel, sessionLabel, addStudTitle, zeroSearchResult;
    public JFXCheckBox male, female;
    public JFXButton addNewStud;
    public Pane preview, modifyStudent;

    private static boolean addNew = true;

    public void addNewStudent() {
        if(addNew) {
            MongoCollection<Document> collection = insertInto("students");
            Bson filter = Filters.eq("id", newStudID.getText());
            Bson update = new Document("$set",
                    new Document()
                            .append("name", newStudName.getText())
                            .append("id", newStudID.getText())
                            .append("dept", newStudDept.getText().toUpperCase())
                            .append("session", newStudSes.getText())
                            .append("gender", male.isSelected() ? "Male" : "Female")
                            .append("admissionDate", newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString())
                            .append("password", BCrypt.hashPassword(newStudPass.getText(), BCrypt.genSalt()))
                            .append("added_on", new Date()));
            UpdateOptions options = new UpdateOptions().upsert(true);
            collection.updateOne(filter, update, options);
            notification.setText("New student added successfully!");
            notification.setTextFill(Color.GREEN);
            if (isDuplicate()) {
                notification.setText("An existing student was updated!");
                notification.setTextFill(Color.CORAL);
            }
            preview.setVisible(true);
            nameLabel.setText(nameLabel.getText() + newStudName.getText());
            idLabel.setText(idLabel.getText() + newStudID.getText());
            deptLabel.setText(deptLabel.getText() + newStudDept.getText());
            adDateLabel.setText(adDateLabel.getText() + (newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString()));
            sessionLabel.setText((sessionLabel.getText() + newStudSes.getText()));
            disableStudentsFields(true);
            addNewStud.setText("Add Another");
            addNew = false;
        } else {
            nameLabel.setText("Name: ");
            idLabel.setText("ID: ");
            deptLabel.setText("Department: ");
            adDateLabel.setText("Admission Date: ");
            sessionLabel.setText("Session: ");
            preview.setVisible(false);
            clearStudentFields();
            disableStudentsFields(false);
            addNewStud.setText("Add");
            addNewStud.setDisable(true);
            addNew = true;
        }
    }

    private void clearStudentFields() {
        newStudName.clear();
        newStudID.clear();
        newStudDept.clear();
        newStudSes.clear();
        newStudAdDate.setValue(null);
        newStudPass.clear();
    }
    private void disableStudentsFields(boolean disable) {
        if(disable) {
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
            newStudSes.setText(object.get("session").toString());
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
    public void genPass() {
        GenPass gp = new GenPass();
        newStudPass.setText(gp.getString());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanProperty b = new SimpleBooleanProperty(AdminDashboardController.modStudent);
        modifyStudent.visibleProperty().bind(b);
        if(modifyStudent.isVisible()) addStudTitle.setText("Modify a Student");
        else addStudTitle.setText("Add a new Student");
        addNewStud.disableProperty().bind(
                Bindings.isEmpty(newStudID.textProperty())
                        .or(Bindings.isEmpty(newStudName.textProperty()))
                        .or(Bindings.isEmpty(newStudDept.textProperty()))
                        .or(Bindings.isEmpty(newStudSes.textProperty()))
                        .or(Bindings.isNull(newStudAdDate.getEditor().textProperty()))
                        .or(Bindings.isEmpty(newStudPass.textProperty()))
        );
    }
}
