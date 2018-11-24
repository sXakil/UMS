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

public class AllFacultiesController implements Initializable {
    public VBox facVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = getCollection("teachers");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            HBox hBox = new HBox();
            Label uNID = new Label(object.get("uNID").toString());
            uNID.setStyle("-fx-padding: 5; -fx-min-width: 170; -fx-alignment: center");
            Label name = new Label(object.get("name").toString());
            name.setStyle("-fx-padding: 5; -fx-min-width: 300; -fx-alignment: center");
            Label dept = new Label(object.get("department").toString());
            dept.setStyle("-fx-padding: 5; -fx-min-width: 120; -fx-alignment: center");
            Label major = new Label(object.get("major").toString());
            major.setStyle("-fx-padding: 5; -fx-min-width: 160; -fx-alignment: center");
            Label position = new Label(object.get("position").toString());
            position.setStyle("-fx-padding: 5; -fx-min-width: 250; -fx-alignment: center");
            hBox.getChildren().addAll(uNID, name, dept, major, position);
            facVBox.getChildren().add(hBox);
        }
    }
    public void closeFacWindow() {
        AdminDashboardController.facList.close();
    }
}
