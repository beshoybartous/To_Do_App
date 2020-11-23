package com.example.todoapp.ui.edit_profile;

import android.net.Uri;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.UserModel;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfilePresenter extends BasePresenter {
    EditProfileView view;
    public EditProfilePresenter(EditProfileView view) {
        super(view);
        this.view=view;
    }

    public void saveData(UserModel userModel){
        user = firebaseAuth.getCurrentUser();
        StorageReference profileImage = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        profileImage.putFile(Uri.parse(userModel.getImageUri())).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        UserProfileChangeRequest profileChangeRequest =
                                new UserProfileChangeRequest
                                        .Builder().
                                        setDisplayName(userModel.getDisplayName()).
                                        setPhotoUri(uri).
                                        build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
                            saveUserData();
                            view.updateUI();
                        });
                    }
            );
        });
    }

    private void saveUserData() {
        UserModel userModel = new UserModel(user.getUid(),user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.set(userModel.toMap());
    }
}
