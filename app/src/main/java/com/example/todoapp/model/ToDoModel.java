package com.example.todoapp.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ToDoModel implements Serializable {
    String id,title,description,categoryID,dueDate;
    boolean isDone;

    public ToDoModel() {
    }


    public ToDoModel(String id, String title, String description,boolean isDone,String categoryID ,String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone=isDone;
        this.categoryID = categoryID;
        this.dueDate=dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id= id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }



    @NonNull
    @Override
    public String toString() {
        return id+" "+title+" "+description+" "+categoryID;
    }

    public Map<String, Object> toMap(){
        return new HashMap<String, Object>() {{
            put("title",title);
            put("description",description);
            put("done",isDone);
            put("categoryID",categoryID);
            put("dueDate",dueDate);
        }
        };
    }

    public static ToDoModel getTodo(DocumentSnapshot documentSnapshot) {
        return new ToDoModel(documentSnapshot.getId(),
                documentSnapshot.getString("title"),
                documentSnapshot.getString("description"),
                documentSnapshot.getBoolean("done"),
                documentSnapshot.getString("categoryID"),
                documentSnapshot.getString("dueDate"));
    }

    public static String getCategoryColor(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.getString("categoryColor");
    }
    public static String getCategoryTitle(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.getString("categoryTitle");
    }
}
