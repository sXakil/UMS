package com.ums.pau;

import com.jfoenix.controls.JFXTextField;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FacultyDashboardController implements Initializable {

    public VBox vBox;
    public JFXTextField idTF, nameTF, deptTF, genTF;
    public JFXTextField searchToEdit;

    private DBCollection dataRetriever(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("UMS");
        return db.getCollection(collName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCollection collection = dataRetriever("students");
        DBCursor students = collection.find();
        setNewHbox(students);
    }

    private void editorFill(String id) {
        DBCollection collection = dataRetriever("students");
        BasicDBObject dbObject = new BasicDBObject("id", id);
        DBCursor cursor = collection.find(dbObject);
        DBObject object = cursor.next();
        nameTF.setText(object.get("name").toString());
        idTF.setText(object.get("id").toString());
        deptTF.setText(object.get("dept").toString());
        genTF.setText(object.get("gender").toString());

    }

    private MongoCollection<Document> initMongo(String collName) {
        return getDocumentMongoCollection(collName);
    }


    static MongoCollection<Document> getDocumentMongoCollection(String collName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase db = mongoClient.getDatabase("UMS");
        return db.getCollection(collName);
    }

    public void findToEdit() {
        vBox.getChildren().clear();
        DBCollection collection = dataRetriever("students");
        BasicDBObject query = new BasicDBObject("id", searchToEdit.getText());
        DBCursor students = collection.find(query);
        setNewHbox(students);
    }

    private void setNewHbox(DBCursor students) {
        while (students.hasNext()) {
            DBObject object = students.next();
            String id = object.get("id").toString();
            String name = object.get("name").toString();
            String dept = object.get("dept").toString();
            String gender = object.get("gender").toString();
            System.out.println(id + " " + name + " " + dept + " " + gender);

            //node[i] = FXMLLoader.load(getClass().getResource("resources/studentsItem.fxml"));
            HBox hb = new HBox();
            hb.setId("hBox-list");
            hb.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hb);

            Label lb1 = new Label(name);
            lb1.setId("name");
            lb1.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb1);

            Label lb2 = new Label(id);
            lb2.setId("id");
            lb2.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb2);

            Label lb3 = new Label(dept);
            lb3.setId("dept");
            lb3.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb3);

            Label lb4 = new Label(gender);
            lb4.setId("gender");
            lb4.setAlignment(Pos.CENTER);
            hb.getChildren().add(lb4);

            hb.setOnMouseClicked(t -> editorFill(id));
        }
        if(students.count() < 1) {
            vBox.getChildren().add(new Label("No records found!"));
            vBox.setAlignment(Pos.CENTER);
        }
    }
}
//    @FXML
//    public void switchToLogin() throws IOException {
//        new SceneSwitcher().switchSceneTo("resources/studentLogin.fxml");
//    }
