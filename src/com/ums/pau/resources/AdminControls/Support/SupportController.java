package com.ums.pau.resources.AdminControls.Support;

import com.jfoenix.controls.JFXTextArea;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.ums.pau.resources.StudentControls.StudentLoginController;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static com.ums.pau.DatabaseHandler.getFrom;
import static com.ums.pau.DatabaseHandler.insertInto;

public class SupportController implements Initializable {
    public VBox msgVBox;
    public JFXTextArea msgTF;
    private String id = StudentLoginController.id;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMsg();
    }

    private  void getMsg() {
        msgVBox.getChildren().clear();
        DBCollection collection = getFrom("supportMessages");
        DBCursor cursor = collection.find();
        cursor.sort(new BasicDBObject("sentOn", 1));
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            Label messageLabel = new Label();
            messageLabel.setAlignment(Pos.CENTER);
            if(object.get("role").toString().equals("admin")) messageLabel.setStyle("-fx-background-color: royalblue; -fx-text-fill: white; -fx-padding: 10 100 10 10; -fx-background-radius: 20");
            else messageLabel.setStyle("-fx-background-color: darkgrey; -fx-padding: 10 10 100 10; -fx-background-radius: 20");
            messageLabel.setText(object.get("messageBody").toString());
            msgVBox.getChildren().add(messageLabel);
        }
        msgTF.clear();
    }
    public void sendMsg() {
        MongoCollection<Document> collection = insertInto("supportMessages");
        BasicDBObject document = new BasicDBObject("id", id)
                .append("messageBody", msgTF.getText())
                .append("sentOn", new Date())
                .append("role", "admin");
        Document doc = new Document(document);
        collection.insertOne(doc);
        getMsg();
    }
}
