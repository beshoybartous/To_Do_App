package com.example.todoapp.ui.authentication.SignUp;

import com.example.todoapp.base.BaseView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public interface SignUpView extends BaseView {
    void updateUI();
    void authError(Task<AuthResult> task);
}
