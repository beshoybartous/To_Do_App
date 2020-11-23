package com.example.todoapp.ui.profile;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.UserModel;
import com.google.firebase.auth.FirebaseUser;

public interface ProfileView extends BaseView {
    void getUserInfo(UserModel userModel);
    void signOut();
}
