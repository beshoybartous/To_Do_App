package com.example.todoapp.ui.authentication.login;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.CategoryModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.List;

public interface LoginView extends BaseView {
    void updateUI();
    void authError(Task<AuthResult> task);
}
