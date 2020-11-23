package com.example.todoapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.databinding.ActivityProfileBinding;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.ui.authentication.login.LoginActivity;
import com.example.todoapp.ui.change_password.ChangePasswordActivity;
import com.example.todoapp.ui.edit_profile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProfileActivity extends BaseActivity<ProfilePresenter, ActivityProfileBinding> implements ProfileView {

    private static final String KEY_USER_MODEL ="user" ;
    UserModel userModel;

    public static void startProfileActivity(Context context, UserModel userModel){
        Intent profileActivityIntent=new Intent(context, ProfileActivity.class);
        profileActivityIntent.putExtra(KEY_USER_MODEL, userModel);
        context.startActivity(profileActivityIntent);
    }

    @Override
    protected ActivityProfileBinding setViewBinding() {
        return ActivityProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ProfilePresenter setPresenter() {
        return new ProfilePresenter(this);
    }

    @Override
    protected void onPostCreated() {
        if(getIntent().getSerializableExtra(KEY_USER_MODEL)!=null){
            userModel= (UserModel) getIntent().getSerializableExtra(KEY_USER_MODEL);
            loadData();
        }

        viewBinding.btnSignOut.setOnClickListener(view -> {
            presenter.signOut();
        });
        viewBinding.btnEdit.setOnClickListener(view -> {
            if(userModel!=null) {
                EditProfileActivity.startEditProfileActivity(this, userModel);
            }
        });
        viewBinding.btnChangePassword.setOnClickListener(view -> {
            ChangePasswordActivity.startChangePasswordActivity(this);
        });
    }

    private void loadData(){
        Picasso.with(viewBinding.getRoot().getContext()).load( userModel.getImageUri()).into(viewBinding.ivUserImage);
        viewBinding.tvEmail.setText(userModel.getEmail());
        viewBinding.tvDisplayName.setText(userModel.getDisplayName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventData(MessageEvent event){
        Log.d("eventbusss", "edit: ");
        presenter.getUserInfo();
    }

    @Override
    public void getUserInfo(UserModel userModel) {
        this.userModel=userModel;
        loadData();
    }

    @Override
    public void signOut() {
        LoginActivity.startLoginActivity(this);
        finish();
    }
}