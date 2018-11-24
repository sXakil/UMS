package com.ums.pau.resources.StudentControls;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.*;
import com.ums.pau.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class StudentLoginController {
    @FXML
    public JFXTextField studentID;
    @FXML
    public JFXPasswordField passWord;
    @FXML
    public javafx.scene.control.Label error;
    @FXML
    public Hyperlink forgotP;

    static String id;
    @FXML
    public void checkLogin() throws IOException {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        @SuppressWarnings("deprecation")
        DB db = mongoClient.getDB("UMS");
        DBCollection collection = db.getCollection("students");
        BasicDBObject bd = new BasicDBObject("id", studentID.getText());
        DBCursor results = collection.find(bd);
        String pass = null;
        try {
            DBObject ob = results.next();
            pass = ob.get("password").toString();
        } catch (Exception ne) {
            System.out.println("Empty input!");
        }
        if (pass != null && passWord.getText().equals(pass)) {
            id = studentID.getText();
            new SceneSwitcher().switchSceneTo("resources/StudentControls/studentDashboard.fxml");
        } else error.setVisible(true);
    }

    public void forgotPass(ActionEvent event) {
        event.getEventType();
        error.setVisible(true);
        forgotP.setVisible(false);
    }

    public void backToHome() {
        try {
            new SceneSwitcher().switchSceneTo("resources/LandingControls/landing.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
