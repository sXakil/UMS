package com.ums.pau;

import com.jfoenix.controls.*;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ums.pau.FacultyDashboardController.getDocumentMongoCollection;

public class AdminDashboardController implements Initializable {
    public static Stage prompt;
    public static String toBeDeleted;
    public Pane delStudentPane;
    public JFXTextField delSearchTF;
    public Label delSearch;
    public Pane delConfirmation;
    public Label delName, delID, delDept, delGen, delAdDate;
    @FXML
    private JFXTextField newStudName, newStudID, newStudDept, newStudSes, newStudPass;
    @FXML
    private JFXTextField newTeacherName, newTeacherPosition, newTeacherMajor, newTeacherDept, newTeacherPass;
    @FXML
    private JFXDatePicker newStudAdDate, neTeacherJD;
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
    public Label searchResult;
    public Label addStudTitle;

    private MongoCollection<Document> initMongo(String collName) {
        return getDocumentMongoCollection(collName);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toDashboardPane();
    }

    public void addNewStudent() {
        if(addNew) {
            MongoCollection<Document> table = initMongo("students");
            Bson filter = Filters.eq("id", newStudID.getText());
            Bson update = new Document("$set",
                    new Document()
                            .append("name", newStudName.getText())
                            .append("id", newStudID.getText())
                            .append("dept", newStudDept.getText().toUpperCase())
                            .append("session", newStudSes.getText())
                            .append("gender", male.isSelected() ? "Male" : "Female")
                            .append("admissionDate", newStudAdDate.getValue() == null ? new Date().toString() : newStudAdDate.getValue().toString())
                            .append("password", newStudPass.getText())
                            .append("added_on", new Date()));
            UpdateOptions options = new UpdateOptions().upsert(true);
            table.updateOne(filter, update, options);
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
            newStudName.setDisable(true);
            newStudID.setDisable(true);
            newStudDept.setDisable(true);
            newStudSes.setDisable(true);
            newStudAdDate.setDisable(true);
            newStudPass.setDisable(true);
            addNewStud.setText("Add Another");
            addNew = false;
        } else {
            nameLabel.setText("Name: ");
            idLabel.setText("ID: ");
            deptLabel.setText("Department: ");
            adDateLabel.setText("Admission Date: ");
            sessionLabel.setText("Session: ");
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
            tNameLabel.setText(tNameLabel.getText() + newTeacherName.getText());
            positionLabel.setText(positionLabel.getText() + newTeacherPosition.getText());
            majorLabel.setText(majorLabel.getText() + newTeacherMajor.getText());
            tDeptLabel.setText((tDeptLabel.getText() + newTeacherDept.getText()));
            jdLabel.setText(jdLabel.getText() + (neTeacherJD.getValue() == null ? new Date().toString() : neTeacherJD.getValue().toString()));
            newTeacherName.setDisable(true);
            newTeacherPosition.setDisable(true);
            newTeacherMajor.setDisable(true);
            newTeacherDept.setDisable(true);
            neTeacherJD.setDisable(true);
            newTeacherPass.setDisable(true);
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
        addTeacherPane.setVisible(false);
        dashBoardPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(false);
        addStudTitle.setText("Add a Student");
        notification.setText("New Student added successfully!");
        addStudPane.setVisible(true);
    }
    public void toAddTeacherPane() {
        isValid = false;
        dashBoardPane.setVisible(false);
        addStudPane.setVisible(false);
        modify.setVisible(false);
        delStudentPane.setVisible(false);
        addTeacherPane.setVisible(true);
    }
    public void toDashboardPane() {
        MongoCollection<Document> tableS = initMongo( "students");
        totalStud.setText(String.valueOf(tableS.countDocuments()));
        MongoCollection<Document> tableT = initMongo( "teacher");
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
        addTeacherPane.setVisible(false);
        dashBoardPane.setVisible(false);
        delStudentPane.setVisible(false);
        modify.setVisible(true);
        addStudTitle.setText("Modify a Student");
        notification.setText("Student modified successfully!");
        addStudPane.setVisible(true);
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

    private DBCollection initMongoDB() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("UMS");
        return db.getCollection("students");
    }

    private boolean isDuplicate() {
        BasicDBObject bd = new BasicDBObject("id", newStudID.getText());
        DBCollection dc = initMongoDB();
        DBCursor cursor = dc.find(bd);
        try {
            DBObject dbo = cursor.next();
            return !Objects.equals(dbo.get("id").toString(), newStudID.getText());
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public void searchStudent() {
        searchResult.setVisible(false);
        boolean checked = false;
        DBCollection collection = initMongoDB();
        BasicDBObject query = new BasicDBObject("id", searchStud.getText());
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            newStudID.setText(object.get("id").toString());
            newStudName.setText(object.get("name").toString());
            newStudDept.setText(object.get("dept").toString());
            newStudSes.setText(object.get("session").toString());
            newStudPass.setText(object.get("password").toString());
            newStudAdDate.setValue(LocalDate.parse(object.get("admissionDate").toString()));
            if (object.get("gender").toString().equals("Male")) {
                male.setSelected(true);
                female.setSelected(false);
            } else {
                female.setSelected(true);
                male.setSelected(false);
            }
            checked = true;
        }
        if (!checked) searchResult.setVisible(true);
    }

    public void cancelDelete() {
        delConfirmation.setVisible(false);
    }

    public void promptDelete() throws IOException {
        toBeDeleted = delID.getText();
        Parent root = FXMLLoader.load(getClass().getResource("resources/deletePrompt.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        prompt = stage;
        stage.show();
    }

    public void delSearchStudent() {
        delSearch.setVisible(false);
        delConfirmation.setVisible(false);
        boolean checked = false;
        DBCollection collection = initMongoDB();
        BasicDBObject query = new BasicDBObject("id", delSearchTF.getText());
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            delID.setText(object.get("id").toString());
            delName.setText(object.get("name").toString());
            delDept.setText(object.get("dept").toString());
            delAdDate.setText(LocalDate.parse(object.get("admissionDate").toString()).toString());
            delGen.setText(object.get("gender").toString().equals("Male") ? "Male" : "Female");
            delConfirmation.setVisible(true);
            checked = true;
        }
        if (!checked) delSearch.setVisible(true);
    }
}