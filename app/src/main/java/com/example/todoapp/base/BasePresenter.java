package com.example.todoapp.base;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class BasePresenter {

    protected BaseView view;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseFirestore db;
    protected FirebaseUser user;

    public BasePresenter(BaseView view) {
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

}