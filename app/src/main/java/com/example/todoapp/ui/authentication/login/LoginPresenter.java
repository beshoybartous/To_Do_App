package com.example.todoapp.ui.authentication.login;

import androidx.annotation.NonNull;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginPresenter extends BasePresenter {
    LoginView view;

    public LoginPresenter(LoginView view) {
        super(view);
        this.view = view;
        firebaseAuth.signOut();

    }

    public void loginUser(LoginActivity loginActivity, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                            getCategories();
                            view.updateUI();
                        } else {
                            view.authError(task);
                        }
                    }
                });
    }

    public void googleLogin(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                         //   Log.d(TAG, "signInWithCredential:success");
                             user = firebaseAuth.getCurrentUser();
                            view.updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            view.authError(task);
                        }

                        // ...
                    }
                });
    }
    public void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             user = firebaseAuth.getCurrentUser();
                            view.updateUI();
                        } else {
                            // If sign in fails, display a message to the user.

                            view.authError(task);
                        }

                        // ...
                    }
                });
    }
    public void getCategories() {
        String userID = user.getUid();
        db.collection("users").document(userID).collection("categories").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SharedPref.addValue(CategoryModel.getCategory(doc));
            }
        });
    }

}
