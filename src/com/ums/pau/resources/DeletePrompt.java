package com.ums.pau.resources;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.ums.pau.AdminDashboardController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static com.ums.pau.StudentDashboardController.getCollection;

public class DeletePrompt implements Initializable {
    public Label confirmLabel;
    public TextField confirm;
    public Label wrong;
    private static String conf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wrong.setVisible(false);
        conf = randomConfirm();
        confirmLabel.setText(conf);
    }

    public void closeWindow() {
        Stage stage = AdminDashboardController.prompt;
        stage.close();
    }

    private DBCollection dataRetriever() {
        return getCollection("students");
    }

    public void doDelete() {
        wrong.setVisible(false);
        if (confirm.getText().equals(conf)) {
            DBCollection collection = dataRetriever();
            BasicDBObject query = new BasicDBObject("id", AdminDashboardController.toBeDeleted);
            collection.findAndRemove(query);
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
