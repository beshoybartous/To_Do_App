package com.example.todoapp.ui.authentication.login;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface LoginView extends BaseView {
    void updateUI(boolean isNewAccount, UserModel userModel);

    void userAlreadyExist();
}
