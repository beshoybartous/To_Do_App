package com.example.todoapp.ui.edit_profile;

import android.net.Uri;
import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.UserModel;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditProfilePresenter extends BasePresenter {
    EditProfileView view;

    public EditProfilePresenter(EditProfileView view) {
        super(view);
        this.view = view;
    }

    public void updateUser(UserModel userModel) {
        user = firebaseAuth.getCurrentUser();
        if(!userModel.getImageUri().equals(String.valueOf(user.getPhotoUrl()))){
            uploadUserImage(userModel);
        }
        else{
            update(userModel);
        }
    }

    private void uploadUserImage(UserModel userModel) {
        Log.d("enteredyhere", "uploadUserImage: ");
        StorageReference profileImage = FirebaseStorage.getInstance().getReference("profilepics/" + user.getUid() + ".jpg");
        profileImage.putFile(Uri.parse(userModel.getImageUri())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(uri -> {
                            if (uri.isSuccessful()) {
                                userModel.setImageUri(String.valueOf(uri.getResult()));
                                update(userModel);
                            } else {
                                view.showError(Objects.requireNonNull(uri.getException()).getMessage());
                            }
                        }
                );
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void update(UserModel userModel) {
        UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest
                        .Builder().
                        setDisplayName(userModel.getDisplayName()).
                        setPhotoUri(Uri.parse(userModel.getImageUri())).
                        build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveUserData(userModel);
            } else {
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void saveUserData(UserModel userModel) {
        userModel.setDisplayName(user.getDisplayName());
        userModel.setImageUri(user.getPhotoUrl().toString());
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.set(userModel.toMap()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                view.updateUI();
            }
            else{
                view.showError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }
}
