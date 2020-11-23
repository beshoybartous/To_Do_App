package com.example.todoapp.ui.mainactivity;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.UserModel;

public class MainPresenter extends BasePresenter {
    MainView view;
    public MainPresenter(MainView view) {
        super(view);
        this.view=view;
    }
    public void getUserInfo() {
        if (user != null) {
            user.getPhotoUrl().getPath();
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String imageUri = task.getResult().getString("imageUri");
                    UserModel userModel = new UserModel(user.getUid(), user.getEmail(), user.getDisplayName(), imageUri);
                    view.getUserInfo(userModel);
                }
            });
        }
    }
}
