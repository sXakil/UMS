package com.ums.pau.resources.AdminControls.DeleteStudent;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;

public class DeletePrompt implements Initializable {
    public Label confirmLabel;
    public TextField confirm;
    public Label wrong;
    private static String conf;
    public Label delName, delID, delDept;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wrong.setVisible(false);
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
            closeWindow();
        } else {
            wrong.setVisible(true);
            conf = randomConfirm();
            confirmLabel.setText(conf);
        }
    }

    private String randomConfirm() {
        String[] str = {"c", "o", "n", "f", "i", "r", "m"};
        Random randomNumber = new Random();
        for (int i = 0; i < 4; i++) {
            int randomNo = randomNumber.nextInt(str.length - 1);
            str[randomNo] = str[randomNo].toUpperCase();
        }
        StringBuilder s = new StringBuilder();
        for (String string : str) {
            s.append(string);
        }
        return s.toString();
    }

    public void hide() {
        wrong.setVisible(false);
    }
}
