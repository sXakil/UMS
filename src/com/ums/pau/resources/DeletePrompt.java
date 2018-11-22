package com.ums.pau.resources;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.ums.pau.AdminDashboardController;
import javafx.stage.Stage;

import static com.ums.pau.StudentDashboardController.getCollection;

public class DeletePrompt {
    public void closeWindow() {
        Stage stage = AdminDashboardController.prompt;
        stage.close();
    }

    private DBCollection dataRetriever(String collName) {
        return getCollection(collName);
    }

    public void doDelete() {
        DBCollection collection = dataRetriever("students");
        BasicDBObject query = new BasicDBObject("id", AdminDashboardController.toBeDeleted);
        collection.findAndRemove(query);
        closeWindow();
    }
}
