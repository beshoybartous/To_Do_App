package com.example.todoapp.ui.authentication.login;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.model.type.AccountType;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class LoginPresenter extends BasePresenter {
    LoginView view;

    public LoginPresenter(LoginView view) {
        super(view);
        this.view = view;
    }

    public void isEmailExist(UserModel userModel) {
        CollectionReference ref = db.collection("users");
        ref.whereEqualTo("email", userModel.getEmail()).whereEqualTo("type", userModel.getType()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    view.updateUI(true, userModel);
                } else {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        if (doc.getString("email").equals(userModel.getEmail()) && doc.getString("type").equals(userModel.getType())) {
                            socialMediaLogin(userModel);
                            return;
                        }
                    }
                    view.userAlreadyExist();
                }
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public void socialMediaLogin(UserModel userModel) {
        AuthCredential credential;
        if (userModel.getType().equals(AccountType.GOOGLE)) {
            credential = GoogleAuthProvider.getCredential(userModel.getIdToken(), null);
        } else {
            credential = FacebookAuthProvider.getCredential(userModel.getIdToken());
        }

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = firebaseAuth.getCurrentUser();
                        getCategories(false, userModel);
                    } else {
                        view.showError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void emailPasswordLogin(LoginActivity loginActivity, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, task -> {
                    if (task.isSuccessful()) {
                        user = firebaseAuth.getCurrentUser();
                        getCategories(false, new UserModel(AccountType.EMAIL, email, "null"));
                    } else {
                        view.showError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void getCategories(boolean isNewAccoount,UserModel userModel) {
        String userID = user.getUid();
        db.collection("users").document(userID).collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    SharedPref.addValue(CategoryModel.getCategory(doc));
                }
                view.updateUI(isNewAccoount,userModel);
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }
}
