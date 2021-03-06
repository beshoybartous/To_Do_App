package com.example.todoapp.ui.splash_screen;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class SplashScreenPresenter extends BasePresenter {
    SplashScreenView view;

    public SplashScreenPresenter(SplashScreenView view) {
        super(view);
        this.view = view;

    }

    public void checkLoggedIn() {
        if (user == null) {
            view.isLoggedIn(false);
        } else {
            getCategories();
        }
    }

    public void getCategories() {
        String userID = user.getUid();
        db.collection("users").document(userID).collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    // categoryModelList.add(CategoryModel.getCategory(doc));
                    SharedPref.addValue(CategoryModel.getCategory(doc));
                }
                view.isLoggedIn(true);
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

}
