package com.example.todoapp.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserModel implements Serializable {
    String id,email,displayName,imageUri;
    public UserModel() {
    }

    public UserModel(String id,String email, String displayName, String imageUri) {
        this.id = id;
        this.email=email;
        this.displayName = displayName;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, Object> toMap(){
        return new HashMap<String, Object>() {{
            put("title",displayName);
            put("imageUri",imageUri);
        }
        };
    }
}
