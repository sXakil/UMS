package com.ums.pau.resources.StudentControls.DashBoard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.ums.pau.resources.StudentControls.StudentLoginController;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class DashBoardController implements Initializable {
    public VBox dashVBox;
    public Label stID, stName, stDept, stAdDate, stGender;

    private String id = StudentLoginController.id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        stID.setText(id);
        stName.setText(object.get("name").toString());
        stGender.setText(object.get("gender").toString());
        stDept.setText(object.get("dept").toString());
        stAdDate.setText(object.get("admissionDate").toString());

        CategoryAxis xAxis = new CategoryAxis();
        //xAxis.setLabel("Subjects");
        NumberAxis yAxis = new NumberAxis();
        //yAxis.setLabel("Marks");
        @SuppressWarnings("unchecked")
        LineChart<String, Number> lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Academic Progress");
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Based on the marks obtained in the completed courses");
        collection = getFrom("results");
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
}
