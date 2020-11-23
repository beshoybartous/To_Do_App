package com.example.todoapp.ui.authentication.SignUp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpPresenter extends BasePresenter {
    SignUpView view;

    public SignUpPresenter(SignUpView view) {
        super(view);
        this.view = view;
    }

    public void signUpUser(SignUpActivity context, String email, String password, String firstName, String lastName, Uri imageUri) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateProfile(firstName, lastName, imageUri);
                        } else {
                            view.authError(task);
                        }
                    }
                });
    }

    private void updateProfile(String firstName, String lastName, Uri imageUri) {
        user = firebaseAuth.getCurrentUser();
        StorageReference profileImage = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        profileImage.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        UserProfileChangeRequest profileChangeRequest =
                                new UserProfileChangeRequest
                                        .Builder().
                                        setDisplayName(firstName + " " + lastName).
                                        setPhotoUri(uri).
                                        build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
                            saveUserData();
                            createCategory();
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


    public void createCategory() {
        if (user != null) {

            String userID = user.getUid();

            DocumentReference ref = db.collection("users").document(userID).collection("categories").document();
            String id = ref.getId();
            CategoryModel categoryModel = new CategoryModel(id, "tasks", "#0E2277");
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap());

            ref = db.collection("users").document(userID).collection("categories").document();
            id = ref.getId();
            categoryModel = new CategoryModel(id, "match", "#EE0202");
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap());


            ref = db.collection("users").document(userID).collection("categories").document();
            id = ref.getId();
            categoryModel = new CategoryModel(id, "work", "#510E0E");
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap());

            ref = db.collection("users").document(userID).collection("categories").document();
            id = ref.getId();
            categoryModel = new CategoryModel(id, "market", "#EEC902");
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap());

            ref = db.collection("users").document(userID).collection("categories").document();
            id = ref.getId();
            categoryModel = new CategoryModel(id, "football", "#0E7720");
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap());

        }
    }
}
