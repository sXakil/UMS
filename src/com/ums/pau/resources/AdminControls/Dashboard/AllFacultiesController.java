package com.ums.pau.resources.AdminControls.Dashboard;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;


public class AllFacultiesController implements Initializable {
    public VBox facVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = getFrom("teachers");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            HBox hBox = new HBox();
            hBox.setId("listItem");

            Label uNID = new Label(object.get("uNID").toString());
            uNID.setStyle("-fx-padding: 5.0; -fx-min-width: 170; -fx-alignment: center");
            uNID.setId("itemChild");

            Label name = new Label(object.get("name").toString());
            name.setStyle("-fx-padding: 5; -fx-min-width: 300; -fx-alignment: center");
            name.setId("itemChild");

            Label dept = new Label(object.get("department").toString());
            dept.setStyle("-fx-padding: 5; -fx-min-width: 120; -fx-alignment: center");
            dept.setId("itemChild");

            Label major = new Label(object.get("major").toString());
            major.setStyle("-fx-padding: 5; -fx-min-width: 160; -fx-alignment: center");
            major.setId("itemChild");

            Label position = new Label(object.get("position").toString());
            position.setStyle("-fx-padding: 5; -fx-min-width: 250; -fx-alignment: center");
            position.setId("itemChild");

            hBox.getChildren().addAll(uNID, name, dept, major, position);
            facVBox.getChildren().add(hBox);
        }
    }
    public void closeFacWindow() {
        DashboardController.facList.close();
    }
}
