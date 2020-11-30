package com.example.todoapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserModel implements Serializable {
    String id, type, email, displayName, imageUri, idToken;

    public UserModel() {
    }

    public UserModel(String type, String email, String idToken) {
        this.type = type;
        this.email = email;
        this.idToken = idToken;
    }

    public UserModel(String id, String email, String displayName, String imageUri) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.imageUri = imageUri;
    }

    public UserModel(String id, String type, String email, String displayName, String imageUri) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.displayName = displayName;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {
            {
                put("email", email);
                put("type", type);
                put("title", displayName);
                put("imageUri", imageUri);
            }
        };
    }
}
