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
import javafx.beans.binding.Bindings;
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
    public Label notification, nameLabel, positionLabel, majorLabel, deptLabel, jdLabel;
    public Pane preview;
    public JFXButton addNewTeacher;
    public JFXDatePicker newTeacherJD;
    public JFXTextField newTeacherUNID;
    public JFXCheckBox male, female;
    public Label zeroFacFound;
    public JFXTextField searchFacTF;


    private static boolean addNewT = true;
    public Pane modifyFaculty;
    public Label addFacultyTitle;

    public void addNewTeacher() {
        if(addNewT) {
            MongoCollection<Document> collection = insertInto("teachers");
            Bson update = new Document("$set",
                    new Document()
                            .append("name", newTeacherName.getText())
                            .append("uNID", newTeacherUNID.getText())
                            .append("position", newTeacherPosition.getText())
                            .append("major", newTeacherMajor.getText())
                            .append("department", newTeacherDept.getText().toUpperCase())
                            .append("joiningDate", newTeacherJD.getValue() == null ? new Date() : newTeacherJD.getValue().toString())
                            .append("password", BCrypt.hashPassword(newTeacherPass.getText(), BCrypt.genSalt()))
                            .append("gender", male.isSelected() ? "Male" : "Female")
                            .append("added_on", new Date()));
            Bson filter = Filters.eq("uNID", newTeacherUNID.getText());
            UpdateOptions options = new UpdateOptions().upsert(true);
            collection.updateOne(filter, update, options);
            preview.setVisible(true);
            nameLabel.setText(nameLabel.getText() + newTeacherName.getText());
            positionLabel.setText(positionLabel.getText() + newTeacherPosition.getText());
            majorLabel.setText(majorLabel.getText() + newTeacherMajor.getText());
            deptLabel.setText((deptLabel.getText() + newTeacherDept.getText()));
            jdLabel.setText(jdLabel.getText() + (newTeacherJD.getValue() == null ? new Date().toString() : newTeacherJD.getValue().toString()));
            disableTeachersFields(true);
            addNewTeacher.setText("Add Another");
            addNewT = false;
        } else {
            notification.setText(notification.getText());
            nameLabel.setText("Name: ");
            positionLabel.setText("Position: ");
            majorLabel.setText("Major: ");
            deptLabel.setText("Department: ");
            jdLabel.setText("Joining Date: ");
            preview.setVisible(false);
            disableTeachersFields(false);
            clearTeachersFields();
            addNewTeacher.setText("Add");
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
            getGender(object.get("gender").toString(), male, female);
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanProperty b = new SimpleBooleanProperty(AdminDashboardController.modFaculty);
        modifyFaculty.visibleProperty().bind(b);
        if(modifyFaculty.isVisible()) addNewTeacher.setText("Modify");
        else addNewTeacher.setText("Add");
        if(modifyFaculty.isVisible()) addFacultyTitle.setText("Modify a Faculty");
        else addFacultyTitle.setText("Add a new Faculty");
        addNewTeacher.disableProperty().bind(
                Bindings.isEmpty(newTeacherName.textProperty())
                        .or(Bindings.isEmpty(newTeacherUNID.textProperty()))
                        .or(Bindings.isEmpty(newTeacherDept.textProperty()))
                        .or(Bindings.isEmpty(newTeacherPosition.textProperty()))
                        .or(Bindings.isNull(newTeacherJD.getEditor().textProperty()))
                        .or(Bindings.isEmpty(newTeacherMajor.textProperty()))
                        .or(Bindings.isEmpty(newTeacherPass.textProperty()))
        );
    }
}
