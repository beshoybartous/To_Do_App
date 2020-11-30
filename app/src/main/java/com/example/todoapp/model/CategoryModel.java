package com.example.todoapp.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryModel implements Serializable {
    String id,title,color;

    public CategoryModel() {
    }

    public CategoryModel(String id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
    public static CategoryModel getCategory(DocumentSnapshot documentSnapshot) {
        return new CategoryModel(documentSnapshot.getId(),
                documentSnapshot.getString("title"),
                documentSnapshot.getString("color"));
    }
    public  Map<String, Object> toMap(){
        return new HashMap<String, Object>() {{
            put("title",title);
            put("color",color);
        }
        };
    }

}
