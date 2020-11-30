package com.example.todoapp.ui.change_password;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.base.BaseView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.Objects;

public class ChangePasswordPresenter extends BasePresenter {
    ChangePasswordView view;
    public ChangePasswordPresenter(ChangePasswordView view) {
        super(view);
        this.view=view;
    }

    public void changePassword(String oldPassword, String newPassword){

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        signOut();
                    }
                    else{
                        view.showError(Objects.requireNonNull(task1.getException()).getMessage());
                    }
                });
            }
            else{
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public void signOut() {
        firebaseAuth.signOut();
        view.signOut();
    }
}
