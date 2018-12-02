package com.ums.pau.resources.StudentControls.GradeReport;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.ums.pau.resources.StudentControls.StudentLoginController;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class GradeReportController implements Initializable {
    public VBox vBox;

    private String id = StudentLoginController.id;

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = getFrom("results");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        query = new BasicDBObject("semester", 1);
        getReport(cursor.sort(query));
    }
}
