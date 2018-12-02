package com.ums.pau.resources.StudentControls.ChangePassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.ums.pau.BCrypt;
import com.ums.pau.resources.ForgottenPasswordPrompt;
import com.ums.pau.resources.StudentControls.StudentLoginController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ums.pau.DatabaseHandler.getFrom;

public class ChangePasswordController implements Initializable {
    public JFXPasswordField oldPass;
    public JFXPasswordField newPass;
    public JFXPasswordField confirmPass;
    public JFXButton changePassButton;
    public Label passSuccess;
    public Label wrongPass;
    public Label passMatch;
    public Label invalidPass;
    private String id = StudentLoginController.id;

    public void validatePassword() {
        Pattern pattern = Pattern.compile("^(?=\\P{Ll}*\\p{Ll})(?=\\P{Lu}*\\p{Lu})(?=\\P{N}*\\p{N})(?=[\\p{L}\\p{N}]*[^\\p{L}\\p{N}])[\\s\\S]{8,}$");
        Matcher matcher = pattern.matcher(newPass.getText());
        if (matcher.matches()) {
            invalidPass.setVisible(false);
            confirmPass.setDisable(false);
        } else {
            invalidPass.setVisible(true);
            confirmPass.setDisable(true);
        }
    }

    public void didMatch() {
        if (confirmPass.getText().equals(newPass.getText())) {
            passMatch.setVisible(false);
            changePassButton.setDisable(false);
        } else {
            passMatch.setVisible(true);
            changePassButton.setDisable(true);
        }
    }

    public void changePassword() {
        passSuccess.setVisible(false);
        wrongPass.setVisible(false);
        DBCollection collection = getFrom("students");
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.next();
        if (BCrypt.checkPassword(oldPass.getText(), object.get("password").toString())) {
            BasicDBObject newPassObject = new BasicDBObject("password", BCrypt.hashPassword(newPass.getText(), BCrypt.genSalt()));
            newPassObject = new BasicDBObject("$set", newPassObject);
            collection.update(query, newPassObject);
            passSuccess.setVisible(true);
        } else {
            wrongPass.setVisible(true);
        }
        changePassButton.setDisable(true);
        newPass.clear();
        oldPass.clear();
        confirmPass.clear();
    }

    public void forgotPass() throws IOException {
        new ForgottenPasswordPrompt().show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changePassButton.setDisable(true);
        passSuccess.setVisible(false);
    }
}
