package com.ums.pau.resources.StudentControls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.mongodb.*;
import com.ums.pau.SceneSwitcher;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
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
    public VBox dashVBox;
    public Label stID, stName, stGender, stDept, stAdDate;
    private String id = StudentLoginController.id;
    public Label studentName;

    private static int i = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toHome();
        changePassButton.setDisable(true);
        passSuccess.setVisible(false);
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        stID.setText(id);
        stName.setText(object.get("name").toString());
        stGender.setText(object.get("gender").toString());
        stDept.setText(object.get("dept").toString());
        stDept.setText(object.get("admissionDate").toString());

        studentName.setText(object.get("name").toString());
        collection = getFrom("results");
        cursor = collection.find(query);
        query = new BasicDBObject("semester", 1);
        getReport(cursor.sort(query));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Subjects");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Marks");
        @SuppressWarnings("unchecked")
        LineChart<String, Number> lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Academic Progress");
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Marks obtained in different courses");
        query = new BasicDBObject("id", id);
        cursor = collection.find(query);
        query = new BasicDBObject("semester", 1);
        DBCursor tc = cursor.sort(query);
        int j = 1;
        while (tc.hasNext()) {
            DBObject obj = tc.next();
            XYChart.Data<String, Number> data = new XYChart.Data<>( String.valueOf(j++), Double.parseDouble(obj.get("mark").toString()));
            dataSeries.getData().add(data);
        }

        lineChart.getData().add(dataSeries);
        dashVBox.getChildren().add(lineChart);
    }

    private void getReport(DBCursor cursor) {
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

            Label cgpa = new Label(markToCGPA(Double.parseDouble(object.get("mark").toString())));
            cgpa.setId("itemS");

            hBox.getChildren().addAll(semester, course, credit, mark, cgpa);
            vBox.getChildren().add(hBox);
        }
    }



    private String markToCGPA(double m) {
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
