package com.example.todoapp.ui.authentication.SignUp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivitySignUpBinding;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.model.type.AccountType;
import com.example.todoapp.ui.mainactivity.MainActivity;
import com.example.todoapp.utils.Constants;
import com.example.todoapp.utils.HelperUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SignUpActivity extends BaseActivity<SignUpPresenter, ActivitySignUpBinding> implements SignUpView {
    private static final String KEY_SOCIAL_SIGN_UP = "account";
    private static final int PICTURE_ID = 123;
    private static final int CAMERA_PERMISSION_REQUEST = 11;
    UserModel userModel = new UserModel();



    public static void startSignUpActivity(Context context, UserModel account) {
        Intent signUpIntent = new Intent(context, SignUpActivity.class);
        signUpIntent.putExtra(KEY_SOCIAL_SIGN_UP, account);
        context.startActivity(signUpIntent);
    }

    @Override
    protected ActivitySignUpBinding setViewBinding() {
        return ActivitySignUpBinding.inflate(getLayoutInflater());
    }

    @Override
    protected SignUpPresenter setPresenter() {
        return new SignUpPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showBackButton();
        viewBinding.fabNewImage.setOnClickListener(view -> selectImage());

        if (getIntent().getSerializableExtra(KEY_SOCIAL_SIGN_UP) != null) {
            userModel = (UserModel) getIntent().getSerializableExtra(KEY_SOCIAL_SIGN_UP);
            viewBinding.etEmail.setText(userModel.getEmail());
            viewBinding.etEmail.setFocusable(false);
            viewBinding.txtInputPassword.setVisibility(View.GONE);
            String[] name = userModel.getDisplayName().split(" ");
            viewBinding.etFirstName.setText(name[0]);
            viewBinding.etLastName.setText(name[1]);
            Picasso.get().load(Uri.parse(userModel.getImageUri())).into(viewBinding.ivUserImage);
        }

        viewBinding.btnSignUp.setOnClickListener(view -> {
            if(onDataValidation()){
                showDialog();
                if (viewBinding.txtInputPassword.getVisibility() == View.GONE) {
                    userModel.setDisplayName(viewBinding.etFirstName.getText().toString() + " " +
                            viewBinding.etLastName.getText().toString());
                    presenter.signUpSocialMedia(userModel);
                } else {
                    userModel.setEmail(viewBinding.etEmail.getText().toString());
                    userModel.setDisplayName(viewBinding.etFirstName.getText().toString() + " " +
                            viewBinding.etLastName.getText().toString());
                    userModel.setType(AccountType.EMAIL);
                    presenter.signUpUser(userModel,
                            viewBinding.etPassword.getText().toString());
                }
            }
        });
    }

    private boolean onDataValidation() {
        viewBinding.txtInputEmail.setError(null);
        viewBinding.txtInptFirstName.setError(null);
        viewBinding.txtInptLastName.setError(null);
        viewBinding.txtInputPassword.setError(null);
        if (HelperUtil.isEditTextEmpty(viewBinding.etFirstName)) {
            viewBinding.txtInptFirstName.setError(Constants.EMPTY_FIRST_NAME);
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etLastName)) {
            viewBinding.txtInptLastName.setError(Constants.EMPTY_LAST_NAME);
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etFirstName)) {
            viewBinding.txtInptFirstName.setError(Constants.EMPTY_FIRST_NAME);
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etEmail)) {
            viewBinding.txtInputEmail.setError(Constants.EMPTY_EMAIL);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(viewBinding.etEmail.getText().toString()).matches()) {
            viewBinding.txtInputEmail.setError(Constants.INVALID_EMAIL_FORMAT);
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etPassword) && viewBinding.txtInputPassword.getVisibility() == View.VISIBLE) {
            viewBinding.txtInputEmail.setError(Constants.EMPTY_PASSWORD);
        } else if (userModel.getImageUri() == null) {
            Toast.makeText(this, Constants.EMPTY_PROFILE_IMAGE, Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void updateUI() {
        hideDialog();
        MainActivity.startMainActivity(this);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select profile image"), PICTURE_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //presenter.uploadPhoto(data.getData());
            userModel.setImageUri(String.valueOf(data.getData()));
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(userModel.getImageUri()));
                viewBinding.ivUserImage.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}