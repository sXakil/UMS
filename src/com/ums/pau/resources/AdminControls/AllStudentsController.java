package com.ums.pau.resources.AdminControls;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import static com.ums.pau.resources.StudentControls.StudentDashboardController.getCollection;

public class AllStudentsController implements Initializable {
    public VBox studVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = getCollection("students");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            HBox hBox = new HBox();
            Label id = new Label(object.get("id").toString());
            id.setStyle("-fx-padding: 5; -fx-min-width: 170; -fx-alignment: center");
            Label name = new Label(object.get("name").toString());
            name.setStyle("-fx-padding: 5; -fx-min-width: 300; -fx-alignment: center");
            Label dept = new Label(object.get("dept").toString());
            dept.setStyle("-fx-padding: 5; -fx-min-width: 120; -fx-alignment: center");
            Label gender = new Label(object.get("gender").toString());
            gender.setStyle("-fx-padding: 5; -fx-min-width: 160; -fx-alignment: center");
            Label admissionDate = new Label(object.get("admissionDate").toString());
            admissionDate.setStyle("-fx-padding: 5; -fx-min-width: 250; -fx-alignment: center");
            hBox.getChildren().addAll(id, name, dept, gender, admissionDate);
            studVBox.getChildren().add(hBox);
        }
    }
    public void closeStudWindow() {
        AdminDashboardController.studList.close();
    }
}
