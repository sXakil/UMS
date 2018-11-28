package com.ums.pau.resources.AdminControls.DeleteStudent;

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

    static Stage prompt;
    static String toBeDeleted;

    public void cancelDelete() {
        delConfirmation.setVisible(false);
        delSearchTF.clear();
    }

    static boolean delAll = false;
    public void promptDelete() throws IOException {
        toBeDeleted = delID.getText();
        Parent root = FXMLLoader.load(getClass().getResource("DeleteStudent/deletePrompt.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        prompt = stage;
        delAll = includeResult.isSelected();
        stage.show();
        delConfirmation.setDisable(true);
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
            delID.setText(object.get("id").toString());
            delName.setText(object.get("name").toString());
            delDept.setText(object.get("dept").toString());
            delAdDate.setText(LocalDate.parse(object.get("admissionDate").toString()).toString());
            delGen.setText(object.get("gender").toString().equals("Male") ? "Male" : "Female");
            delConfirmation.setVisible(true);
            delConfirmation.setDisable(false);
            checked = true;
        }
        if (!checked) delSearch.setVisible(true);
    }
}
