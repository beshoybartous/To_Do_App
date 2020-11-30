package com.example.todoapp.ui.profile;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.UserModel;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class ProfilePresenter extends BasePresenter {
    ProfileView view;

    public ProfilePresenter(ProfileView view) {
        super(view);
        this.view = view;
    }

    public void getUserInfo() {
        user.getPhotoUrl().getPath();
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUri = task.getResult().getString("imageUri");
                UserModel userModel = new UserModel(user.getUid(), user.getEmail(), user.getDisplayName(), imageUri);
                view.getUserInfo(userModel);
            }
        });
    }

    public void signOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        view.signOut();
    }

    public void checkAccountType() {
        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("facebook.com")) {
                //For linked facebook account
                view.socialMediaLogin();
            } else if (user.getProviderId().equals("google.com")) {
                //For linked Google account
                view.socialMediaLogin();

            }
            else{
                Log.d("xx_xx_provider_info", user.getProviderId());

            }
        }
    }
}
