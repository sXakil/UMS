package com.ums.pau;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.mongodb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FacultyDashboardController implements Initializable {
    @FXML
    public JFXTextField searchTF;
    @FXML
    public JFXTextField idTF;
    @FXML
    public JFXTextField nameTF;
    @FXML
    public JFXTextField dptTF;
    @FXML
    public JFXTextField cgpaTF;
    @FXML
    public Label idLB;
    @FXML
    public Label nameLB;
    @FXML
    public Label dptLB;
    @FXML
    public Label cgpaLB;
    public VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("UMS");
        DBCollection collection = db.getCollection("students");
        DBCursor students = collection.find();
        int count = students.count();
        int i = 0;
        Node[] node = new Node[count];
        while (students.hasNext()) {
            DBObject object = students.next();
            String id = object.get("id").toString();
            String name = object.get("name").toString();
            String dept = object.get("dept").toString();
            String gender = object.get("gender").toString();
            try {
                node[i] = FXMLLoader.load(getClass().getResource("resources/studentsItem.fxml"));
                vBox.getChildren().add(node[i]);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//    @FXML
//    public void switchToLogin() throws IOException {
//        new SceneSwitcher().switchSceneTo("resources/studentLogin.fxml");
//    }
}