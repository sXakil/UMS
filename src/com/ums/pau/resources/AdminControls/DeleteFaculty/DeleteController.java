package com.ums.pau.resources.AdminControls.DeleteFaculty;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.ums.pau.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;

import static com.ums.pau.DatabaseHandler.getFrom;

public class DeleteController {
    public JFXTextField delSearchTF;
    public Label delSearch;
    public Pane delConfirmation;
    public Label delName, delID, delDept, delGen, delAdDate;

    static Stage deletePrompt;
    static String dID;
    static String dName;
    static String dDept;

    public void cancelDelete() {
        delConfirmation.setVisible(false);
        delSearchTF.clear();
    }

    public void promptDelete() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("deletePrompt.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        deletePrompt = stage;
        stage.show();
        new Shake(root).play();
        delConfirmation.setVisible(false);
        delSearchTF.clear();
    }

    public void delSearchFaculty() {
        delSearch.setVisible(false);
        delConfirmation.setVisible(false);
        boolean checked = false;
        DBCollection collection = getFrom("teachers");
        BasicDBObject query = new BasicDBObject("uNID", delSearchTF.getText().replaceAll("-", ""));
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            dID = object.get("uNID").toString();
            delID.setText(dID);
            dName = object.get("name").toString();
            delName.setText(dName);
            dDept = object.get("department").toString();
            delDept.setText(dDept);
            delAdDate.setText(LocalDate.parse(object.get("joiningDate").toString()).toString());
            delGen.setText(object.get("position").toString());
            delConfirmation.setVisible(true);
            delConfirmation.setDisable(false);
            checked = true;
        }
        if (!checked) delSearch.setVisible(true);
    }
}
