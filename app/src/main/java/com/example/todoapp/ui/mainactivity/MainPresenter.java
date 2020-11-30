package com.example.todoapp.ui.mainactivity;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.UserModel;

import java.util.Objects;

public class MainPresenter extends BasePresenter {
    MainView view;
    public MainPresenter(MainView view) {
        super(view);
        this.view=view;
    }
    public void getUserInfo() {
        if (user != null) {
            Log.d("userImage", user.getPhotoUrl().toString());
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel userModel = new UserModel(user.getUid(), user.getEmail(), user.getDisplayName(), String.valueOf(user.getPhotoUrl()));
                    userModel.setType(task.getResult().getString("type"));
                    Log.d("calledthereee", user.getPhotoUrl().toString());
                    view.getUserInfo(userModel);
                }
                else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }
}
