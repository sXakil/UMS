package com.ums.pau.resources.AdminControls.DeleteStudent;

import animatefx.animation.FadeOut;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;
import static com.ums.pau.resources.AdminControls.DeleteFaculty.DeletePrompt.randomConfirm;

public class DeletePrompt implements Initializable {
    public Label confirmLabel;
    public TextField confirm;
    public Label wrong;
    private static String conf;
    public Label delName, delID, delDept;
    public AnchorPane ap1, ap2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wrong.setVisible(false);
        ap1.setVisible(true);
        ap2.setVisible(false);
        conf = randomConfirm();
        confirmLabel.setText(conf);
        delName.setText("Name: " + DeleteController.dName);
        delID.setText("ID: " + DeleteController.dID);
        delDept.setText("Department: " + DeleteController.dDept);
    }

    public void closeWindow() {
        Stage stage = DeleteController.deletePrompt;
        stage.close();
    }

    public void doDelete() {
        wrong.setVisible(false);
        if (confirm.getText().equals(conf)) {
            DBCollection collection = getFrom("students");
            BasicDBObject query = new BasicDBObject("id", DeleteController.dID);
            DBCollection backup = getFrom("UMSBackup");
            backup.insert(collection.findOne(query));
            collection.findAndRemove(query);
            if (DeleteController.delAll) {
                DBCollection result = getFrom("results");
                DBCursor cursor = result.find(query);
                while (cursor.hasNext()) {
                    DBObject object = cursor.next();
                    backup.insert(object);
                    result.findAndRemove(object);
                }
            }
            ap1.setVisible(false);
            ap2.setVisible(true);
        } else {
            wrong.setVisible(true);
            conf = randomConfirm();
            confirmLabel.setText(conf);
        }
    }

    public void hide() {
        wrong.setVisible(false);
    }
}
