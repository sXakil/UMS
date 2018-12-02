package com.ums.pau.resources.AdminControls.DeleteFaculty;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
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
        delID.setText("uNID: " + DeleteController.dID);
        delDept.setText("Department: " + DeleteController.dDept);
    }

    public void closeWindow() {
        Stage stage = DeleteController.deletePrompt;
        stage.close();
    }

    public void doDelete() {
        wrong.setVisible(false);
        if (confirm.getText().equals(conf)) {
            DBCollection collection = getFrom("teachers");
            BasicDBObject query = new BasicDBObject("uNID", DeleteController.dID);
            DBCollection backup = getFrom("UMSBackup");
            backup.insert(collection.findOne(query));
            collection.findAndRemove(query);
            closeWindow();
        } else {
            wrong.setVisible(true);
            conf = randomConfirm();
            confirmLabel.setText(conf);
        }
    }


    public static String randomConfirm() {
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
