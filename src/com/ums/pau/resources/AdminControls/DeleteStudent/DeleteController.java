package com.ums.pau.resources.AdminControls.DeleteStudent;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
    public JFXCheckBox includeResult;

    static Stage deletePrompt;
    static String dID;
    static String dName;
    static String dDept;

    public void cancelDelete() {
        delConfirmation.setVisible(false);
        delSearchTF.clear();
    }

    static boolean delAll = false;
    public void promptDelete() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("deletePrompt.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        deletePrompt = stage;
        delAll = includeResult.isSelected();
        stage.show();
        new Shake(root).play();
        delConfirmation.setVisible(false);
        delSearchTF.clear();
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
            dID = object.get("id").toString();
            delID.setText(dID);
            dName = object.get("name").toString();
            delName.setText(dName);
            dDept = object.get("dept").toString();
            delDept.setText(dDept);
            delAdDate.setText(LocalDate.parse(object.get("admissionDate").toString()).toString());
            delGen.setText(object.get("gender").toString().equals("Male") ? "Male" : "Female");
            delConfirmation.setVisible(true);
            delConfirmation.setDisable(false);
            checked = true;
        }
        if (!checked) delSearch.setVisible(true);
    }
}
