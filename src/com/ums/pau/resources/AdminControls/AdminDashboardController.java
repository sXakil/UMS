package com.ums.pau.resources.AdminControls;

import com.jfoenix.controls.*;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.ums.pau.BCrypt;
import com.ums.pau.resources.GenPass;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.*;

public class AdminDashboardController implements Initializable {
    static Stage prompt;
    static String toBeDeleted;
    public Pane delStudentPane;
    public JFXTextField delSearchTF;
    public Label delSearch;
    public Pane delConfirmation;
    public Label delName, delID, delDept, delGen, delAdDate;
    public JFXCheckBox includeResult;
    public JFXTextField newTeacherUNID;
    public Label addFac;
    public JFXTextField searchFacTF;
    public Label facSearchResult;
    public Pane modifyFac;
    @FXML
    private JFXTextField newStudName, newStudID, newStudDept, newStudSes, newStudPass;
    @FXML
    private JFXTextField newTeacherName, newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass;
    @FXML
    private JFXDatePicker newStudAdDate, newTeacherJD;
    @FXML
    private JFXButton addNewStud, addNewTeacher;
    @FXML
    private Label notification, nameLabel, idLabel, deptLabel, adDateLabel, sessionLabel;
    @FXML
    private Label tNotification, tNameLabel, positionLabel, majorLabel, tDeptLabel, jdLabel;
    @FXML
    private Pane dashBoardPane, addStudPane, addTeacherPane;
    @FXML
    private Label totalFac, totalStud;
    @FXML
    private JFXCheckBox male, female, maleT, femaleT;
    @FXML
    private Pane preview, previewT;

    private static boolean isValid = false;
    private static boolean addNew = true;
    private static boolean addNewT = true;
    public Pane modify;
    public JFXTextField searchStud;
    public Label zeroSearchResult;
    public Label addStudTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toDashboardPane();
    }

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

    private void clearStudentFields() {
        newStudName.clear();
        newStudID.clear();
        newStudDept.clear();
        newStudSes.clear();
        newStudAdDate.setValue(null);
        newStudPass.clear();
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
        clearStudentFields();
        isValid = false;
        addTeacherPane.setVisible(false);
        dashBoardPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(false);
        addStudTitle.setText("Add a Student");
        notification.setText("New Student added successfully!");
        addStudPane.setVisible(true);
    }
    public void toAddTeacherPane() {
        clearTeachersFields();
        isValid = false;
        dashBoardPane.setVisible(false);
        addStudPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(false);
        modifyFac.setVisible(false);
        addFac.setText("Add a Faculty");
        addTeacherPane.setVisible(true);
    }
    public void toDashboardPane() {
        MongoCollection<Document> tableS = insertInto( "students");
        totalStud.setText(String.valueOf(tableS.countDocuments()));
        MongoCollection<Document> tableT = insertInto("teachers");
        totalFac.setText(String.valueOf(tableT.countDocuments()));
        addTeacherPane.setVisible(false);
        addStudPane.setVisible(false);
        dashBoardPane.setVisible(true);
        delStudentPane.setVisible(false);
    }

    public void toDeleteStudent() {
        addTeacherPane.setVisible(false);
        dashBoardPane.setVisible(false);
        addStudPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(true);

    }
    public void toModifyStudent() {
        clearStudentFields();
        searchStud.clear();
        addTeacherPane.setVisible(false);
        dashBoardPane.setVisible(false);
        delStudentPane.setVisible(false);
        modify.setVisible(true);
        addStudTitle.setText("Modify a Student");
        notification.setText("Student modified successfully!");
        addStudPane.setVisible(true);
    }

    public void toModifyFac() {
        clearTeachersFields();
        searchFacTF.clear();
        isValid = false;
        dashBoardPane.setVisible(false);
        addStudPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(false);
        addFac.setText("Modify a Faculty");
        modifyFac.setVisible(true);
        addTeacherPane.setVisible(true);
    }

    public void toDeleteFac() {

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
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
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
            //newStudPass.setText(object.get("password").toString());
            newStudAdDate.setValue(LocalDate.parse(object.get("admissionDate").toString()));
            getGender(object.get("gender").toString(), male, female);
            checked = true;
        }
        if (!checked) zeroSearchResult.setVisible(true);
    }

    public void cancelDelete() {
        delConfirmation.setVisible(false);
        delSearchTF.clear();
    }

    static boolean delAll = false;
    public void promptDelete() throws IOException {
        toBeDeleted = delID.getText();
        Parent root = FXMLLoader.load(getClass().getResource("deletePrompt.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        prompt = stage;
        delAll = includeResult.isSelected();
        stage.show();
        delConfirmation.setDisable(true);
    }

    public void delSearchStudent() {
        delSearch.setVisible(false);
        delConfirmation.setVisible(false);
        boolean checked = false;
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", delSearchTF.getText().replaceAll("-", ""));
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            delID.setText(object.get("id").toString());
            delName.setText(object.get("name").toString());
            delDept.setText(object.get("dept").toString());
            delAdDate.setText(LocalDate.parse(object.get("admissionDate").toString()).toString());
            delGen.setText(object.get("gender").toString().equals("Male") ? "Male" : "Female");
            delConfirmation.setVisible(true);
            delConfirmation.setDisable(false);
            checked = true;
        }
        if (!checked) delSearch.setVisible(true);
    }

    public void searchFac() {

        facSearchResult.setVisible(false);
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
        if (!checked) facSearchResult.setVisible(true);
    }

    private void getGender(String string, JFXCheckBox maleT, JFXCheckBox femaleT) {
        if (string.equals("Male")) {
            maleT.setSelected(true);
            femaleT.setSelected(false);
        } else {
            femaleT.setSelected(true);
            maleT.setSelected(false);
        }
    }
    static Stage facList;
    public void showFacList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("allFaculties.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Faculties");
        facList = stage;
        stage.show();
    }
    static Stage studList;
    public void showStudList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("allStudents.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Students");
        studList = stage;
        stage.show();
    }
}
