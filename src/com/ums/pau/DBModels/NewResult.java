package com.ums.pau.DBModels;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.ums.pau.DatabaseHandler.insertInto;

public class NewResult {
    private String id;
    private String semester;
    private String courseCode;
    private double mark;
    private boolean isThree;
    private String comment;
    private String addedBy;

    public NewResult(
            String id,
            String semester,
            String courseCode,
            double mark,
            boolean isThree,
            String comment,
            String addedBy) {
        this.id = id;
        this.semester = semester;
        this.courseCode = courseCode;
        this.mark = mark;
        this.isThree = isThree;
        this.comment = comment;
        this.addedBy = addedBy;
    }

    public void insert() {
        MongoCollection<Document> grades = insertInto("results");
        BasicDBObject dbObject = new BasicDBObject("id", id)
                .append("semester", semester)
                .append("course_code", courseCode.toUpperCase().replaceAll("[ \\-]", ""))
                .append("mark", mark)
                .append("course_credit", isThree ? 3 : 1)
                .append("comment", comment)
                .append("added_by", addedBy);
        Document doc = new Document(dbObject);
        grades.insertOne(doc);
    }
}
