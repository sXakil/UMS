package com.ums.pau.resources.AdminControls.UpsertFaculty;

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
import com.ums.pau.resources.GenPass;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;
import static com.ums.pau.DatabaseHandler.insertInto;

public class AddModifyFaculty implements Initializable {
    public JFXTextField newTeacherName, newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass;
    public Label tNotification, tNameLabel, positionLabel, majorLabel, tDeptLabel, jdLabel;
    public Pane previewT;
    public JFXButton addNewTeacher;
    public JFXDatePicker newTeacherJD;
    public JFXTextField newTeacherUNID;
    public JFXCheckBox maleT, femaleT;
    public Label zeroFacFound;
    private static boolean isValid = false;
    public JFXTextField searchFacTF;


    private static boolean addNewT = true;
    public Pane modifyFaculty;

    public void addNewTeacher() {
        if(addNewT) {
            MongoCollection<Document> collection = insertInto("teachers");
            Bson update = new Document("$set",
                    new Document()
                            .append("name", newTeacherName.getText())
                            .append("uNID", newTeacherUNID.getText())
                            .append("position", newTeacherPosition.getText())
                            .append("major", newTeacherMajor.getText())
                            .append("department", newTeacherDept.getText())
                            .append("joiningDate", newTeacherJD.getValue() == null ? new Date() : newTeacherJD.getValue().toString())
                            .append("password", BCrypt.hashPassword(newTeacherPass.getText(), BCrypt.genSalt()))
                            .append("gender", maleT.isSelected() ? "Male" : "Female")
                            .append("added_on", new Date()));
            Bson filter = Filters.eq("uNID", newTeacherUNID.getText());
            UpdateOptions options = new UpdateOptions().upsert(true);
            collection.updateOne(filter, update, options);
            previewT.setVisible(true);
            tNameLabel.setText(tNameLabel.getText() + newTeacherName.getText());
            positionLabel.setText(positionLabel.getText() + newTeacherPosition.getText());
            majorLabel.setText(majorLabel.getText() + newTeacherMajor.getText());
            tDeptLabel.setText((tDeptLabel.getText() + newTeacherDept.getText()));
            jdLabel.setText(jdLabel.getText() + (newTeacherJD.getValue() == null ? new Date().toString() : newTeacherJD.getValue().toString()));
            disableTeachersFields(true);
            addNewTeacher.setText("Add Another");
            addNewT = false;
        } else {
            tNotification.setText(tNotification.getText());
            tNameLabel.setText("Name: ");
            positionLabel.setText("Position: ");
            majorLabel.setText("Major: ");
            tDeptLabel.setText("Department: ");
            jdLabel.setText("Joining Date: ");
            previewT.setVisible(false);
            disableTeachersFields(false);
            clearTeachersFields();
            addNewTeacher.setText("Add");
            addNewTeacher.setDisable(true);
            addNewT = true;
        }
    }

    private void disableTeachersFields(boolean disable) {
        if(disable) {
            newTeacherName.setDisable(true);
            newTeacherUNID.setDisable(true);
            newTeacherPosition.setDisable(true);
            newTeacherMajor.setDisable(true);
            newTeacherDept.setDisable(true);
            newTeacherJD.setDisable(true);
            newTeacherPass.setDisable(true);
        } else {
            newTeacherName.setDisable(false);
            newTeacherUNID.setDisable(false);
            newTeacherPosition.setDisable(false);
            newTeacherMajor.setDisable(false);
            newTeacherDept.setDisable(false);
            newTeacherJD.setDisable(false);
            newTeacherPass.setDisable(false);
        }
    }

    private void clearTeachersFields() {
        newTeacherUNID.clear();
        newTeacherName.clear();
        newTeacherPosition.clear();
        newTeacherMajor.clear();
        newTeacherDept.clear();
        newTeacherJD.setValue(null);
        newTeacherPass.clear();
    }

    public void searchFac() {
        zeroFacFound.setVisible(false);
        boolean checked = false;
        DBCollection collection = getFrom("teachers");
        BasicDBObject query = new BasicDBObject("uNID", searchFacTF.getText());
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            newTeacherUNID.setText(object.get("uNID").toString());
            newTeacherDept.setText(object.get("department").toString());
            newTeacherName.setText(object.get("name").toString());
            newTeacherPosition.setText(object.get("position").toString());
            newTeacherMajor.setText(object.get("major").toString());
            newTeacherJD.setValue(LocalDate.parse(object.get("joiningDate").toString()));
            getGender(object.get("gender").toString(), maleT, femaleT);
            checked = true;
        }
        if (!checked) zeroFacFound.setVisible(true);
    }
    private void getGender(String string, JFXCheckBox maleT, JFXCheckBox femaleT) {
        toGender(string, maleT, femaleT);
    }

    public static void toGender(String string, JFXCheckBox maleT, JFXCheckBox femaleT) {
        if (string.equals("Male")) {
            maleT.setSelected(true);
            femaleT.setSelected(false);
        } else {
            femaleT.setSelected(true);
            maleT.setSelected(false);
        }
    }

    public void genTPass() {
        GenPass gp = new GenPass();
        newTeacherPass.setText(gp.getString());
        teacherTextListener();
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



    public void teacherTextListener() {
        JFXTextField[] teacherTextFields = {newTeacherName, newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass};
        TextFieldChangedListener(teacherTextFields, addNewTeacher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanProperty b = new SimpleBooleanProperty(AdminDashboardController.modFaculty);
        modifyFaculty.visibleProperty().bind(b);
    }
}
