package com.example.todoapp.ui.profile;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.UserModel;

public class ProfilePresenter extends BasePresenter {
    ProfileView view;
    public ProfilePresenter(ProfileView view) {
        super(view);
        this.view=view;
    }
    public void getUserInfo(){
        Log.d("urlofimage", user.getPhotoUrl().getPath());
        user.getPhotoUrl().getPath();
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String imageUri= task.getResult().getString("imageUri");
                UserModel userModel=new UserModel(user.getUid(),user.getEmail(),user.getDisplayName(),imageUri);
                view.getUserInfo(userModel);
            }
        });
    }
    public void signOut() {
        firebaseAuth.signOut();
        view.signOut();
    }
}
