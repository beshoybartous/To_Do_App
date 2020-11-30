package com.example.todoapp.ui.authentication.SignUp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class SignUpPresenter extends BasePresenter {
    SignUpView view;

    public SignUpPresenter(SignUpView view) {
        super(view);
        this.view = view;
    }

    public void signUpSocialMedia(UserModel userModel) {
        AuthCredential credential;
        if (userModel.getType().equals("google")) {
            credential = GoogleAuthProvider.getCredential(userModel.getIdToken(), null);
        } else {
            credential = FacebookAuthProvider.getCredential(userModel.getIdToken());
        }
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = firebaseAuth.getCurrentUser();
                        uploadUserImage(userModel);
                    } else {
                        view.showError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void signUpUser(UserModel userModel, String password) {
        firebaseAuth.createUserWithEmailAndPassword(userModel.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadUserImage(userModel);
                        } else {
                            view.showError(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    private void uploadUserImage(UserModel userModel) {
        user = firebaseAuth.getCurrentUser();
        StorageReference profileImage = FirebaseStorage.getInstance().getReference("profilepics/" + user.getUid() + ".jpg");
        profileImage.putFile(Uri.parse(userModel.getImageUri())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(uri -> {
                    if (uri.isSuccessful()) {
                        updateUserData(userModel,String.valueOf(uri.getResult()));

                    } else {
                        view.showError(Objects.requireNonNull(uri.getException()).getMessage());
                    }
                });
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void updateUserData(UserModel userModel, String uri) {
        userModel.setImageUri(uri);
        UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest
                        .Builder().
                        setDisplayName(userModel.getDisplayName()).
                        setPhotoUri(Uri.parse(userModel.getImageUri())).
                        build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveUserData(userModel.getType());
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void saveUserData(String emailType) {
        UserModel userModel = new UserModel(user.getUid(), emailType, user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.set(userModel.toMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.updateUI();
            }
            else{
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

}
