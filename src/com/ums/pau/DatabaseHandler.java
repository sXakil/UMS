package com.ums.pau;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    static private MongoClient initMongo() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        return new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    }

    public static MongoCollection<Document> insertInto(String collectionName) {
        MongoDatabase db = initMongo().getDatabase("UMS");
        return db.getCollection(collectionName);
    }

    public static DBCollection getFrom(String collectionName) {
        DB db = initMongo().getDB("UMS");
        return db.getCollection(collectionName);
    }
}
