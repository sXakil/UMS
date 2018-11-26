package com.ums.pau.resources.StudentControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.mongodb.*;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ums.pau.DatabaseHandler.getFrom;

public class StudentDashboardController implements Initializable {
    public Pane home, gradeReport, changePass;
    public VBox vBox;
    public JFXPasswordField oldPass, newPass, confirmPass;
    public Label passSuccess, invalidPass, wrongPass, passMatch;
    public JFXButton changePassButton;
    public Pane support;
    private String id = StudentLoginController.id;
    public Label studentName;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toHome();
        changePassButton.setDisable(true);
        passSuccess.setVisible(false);
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        studentName.setText(object.get("name") == null ? "Unknown" : object.get("name").toString());
        collection = getFrom("results");
        cursor = collection.find(query);
        query = new BasicDBObject("semester", 1);
        getReport(cursor.sort(query));
    }

    private void getReport(DBCursor cursor) {
        int i = 1;
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            if (object.get("semester").toString().contains(String.valueOf(i))) {
                HBox hBox = new HBox();
                hBox.setId("hBox-header");
                Label semester = new Label("Semester");
                semester.setId("itemH");
                Label course = new Label("Course Code");
                course.setId("itemH");
                Label credit = new Label("Credit");
                credit.setId("itemH");
                Label mark = new Label("Mark");
                mark.setId("itemH");
                Label cgpa = new Label("Grade");
                cgpa.setId("itemH");
                hBox.getChildren().addAll(semester, course, credit, mark, cgpa);
                if (i > 1) VBox.setMargin(hBox, new Insets(25, 0, 0, 0));
                vBox.getChildren().add(hBox);
                i++;
            }
            HBox hBox = new HBox();
            hBox.setId("hBox-list");
            Label semester = new Label(object.get("semester").toString());
            semester.setId("itemS");

            Label course = new Label(object.get("course_code").toString());
            course.setId("itemS");

            Label mark = new Label(object.get("mark").toString());
            mark.setId("itemS");

            Label credit = new Label(object.get("course_credit").toString());
            credit.setId("itemS");

            Label cgpa = new Label(getGrade(object.get("mark").toString()));
            cgpa.setId("itemS");

            hBox.getChildren().addAll(semester, course, credit, mark, cgpa);
            vBox.getChildren().add(hBox);
        }
    }


    private String getGrade(String mark) {
        double m = Double.parseDouble(mark);
        return markToCGPA(m);
    }

    public static String markToCGPA(double m) {
        if (m >= 80) return "4.00";
        else if (m >= 75) return "3.75";
        else if (m >= 70) return "3.50";
        else if (m >= 65) return "3.25";
        else if (m >= 60) return "3.00";
        else if (m >= 55) return "2.75";
        else if (m >= 50) return "2.50";
        else if (m >= 45) return "2.25";
        else if (m >= 40) return "2.00";
        else return "0.00";
    }


    public void toHome() {
        gradeReport.setVisible(false);
        changePass.setVisible(false);
        support.setVisible(false);
        home.setVisible(true);
    }

    public void toGrade() {
        home.setVisible(false);
        changePass.setVisible(false);
        support.setVisible(false);
        gradeReport.setVisible(true);
    }

    public void toCPass() {
        home.setVisible(false);
        gradeReport.setVisible(false);
        support.setVisible(false);
        changePass.setVisible(true);

    }

    public void toSupport() {
        home.setVisible(false);
        gradeReport.setVisible(false);
        changePass.setVisible(false);
        support.setVisible(true);
    }

    public void logOut() throws IOException {
        new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
    }

    public void validatePassword() {
        Pattern pattern = Pattern.compile("^(?=\\P{Ll}*\\p{Ll})(?=\\P{Lu}*\\p{Lu})(?=\\P{N}*\\p{N})(?=[\\p{L}\\p{N}]*[^\\p{L}\\p{N}])[\\s\\S]{8,}$");
        Matcher matcher = pattern.matcher(newPass.getText());
        if (matcher.matches()) {
            invalidPass.setVisible(false);
            confirmPass.setDisable(false);
        } else {
            invalidPass.setVisible(true);
            confirmPass.setDisable(true);
        }
    }

    public void didMatch() {
        if (confirmPass.getText().equals(newPass.getText())) {
            passMatch.setVisible(false);
            changePassButton.setDisable(false);
        } else {
            passMatch.setVisible(true);
            changePassButton.setDisable(true);
        }
    }

    public void changePassword() {
        passSuccess.setVisible(false);
        wrongPass.setVisible(false);
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        String pass = object.get("password").toString();
        if (pass.equals(oldPass.getText())) {
            BasicDBObject newPassObject = new BasicDBObject("password", newPass.getText());
            newPassObject = new BasicDBObject("$set", newPassObject);
            collection.update(query, newPassObject);
            passSuccess.setVisible(true);
        } else {
            wrongPass.setVisible(true);
        }
        changePassButton.setDisable(true);
        newPass.clear();
        oldPass.clear();
        confirmPass.clear();
    }

    public void forgotPass() {

    }
}
