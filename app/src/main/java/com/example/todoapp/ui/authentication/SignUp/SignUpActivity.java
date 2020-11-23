package com.example.todoapp.ui.authentication.SignUp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivitySignUpBinding;
import com.example.todoapp.ui.mainactivity.MainActivity;
import com.example.todoapp.utils.Constants;
import com.example.todoapp.utils.HelperUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.io.IOException;

public class SignUpActivity extends BaseActivity<SignUpPresenter, ActivitySignUpBinding> implements SignUpView {
    Intent cam;
    Uri imageUri = null;
    private static final int PICTURE_ID = 123;
    private static final int CAMERA_PERMISSION_REQUEST = 11;

    public static void startSignUpActivity(Context context) {
        Intent toDoIntent = new Intent(context, SignUpActivity.class);
        context.startActivity(toDoIntent);
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
        cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        viewBinding.fabNewImage.setOnClickListener(view -> selectImage());


        viewBinding.btnSignUp.setOnClickListener(view -> {
            viewBinding.txtInputEmail.setError(null);
            viewBinding.txtInptFirstName.setError(null);
            viewBinding.txtInptLastName.setError(null);
            viewBinding.txtInputPassword.setError(null);
            if(HelperUtil.isEditTextEmpty(viewBinding.etFirstName)){
                viewBinding.txtInptFirstName.setError(Constants.EMPTY_FIRST_NAME);
            }
            else if(HelperUtil.isEditTextEmpty(viewBinding.etLastName)){
                viewBinding.txtInptFirstName.setError(Constants.EMPTY_LAST_NAME);
            }
            else if(HelperUtil.isEditTextEmpty(viewBinding.etFirstName)){
                viewBinding.txtInptFirstName.setError(Constants.EMPTY_FIRST_NAME);
            }
            else if(HelperUtil.isEditTextEmpty(viewBinding.etEmail)){
                viewBinding.txtInptFirstName.setError(Constants.EMPTY_EMAIL);
            }
            else if (Patterns.EMAIL_ADDRESS.matcher(viewBinding.etEmail.getText().toString()).matches()) {
                viewBinding.txtInputEmail.setError(Constants.INVALID_EMAIL_FORMAT);
            }
            else if(HelperUtil.isEditTextEmpty(viewBinding.etPassword)){
                viewBinding.txtInputEmail.setError(Constants.EMPTY_PASSWORD);
            }
            else if(imageUri==null){
                Toast.makeText(this, Constants.EMPTY_PROFILE_IMAGE, Toast.LENGTH_LONG).show();
            }
            else {
                showDialog();
                presenter.signUpUser(SignUpActivity.this, viewBinding.etEmail.getText().toString(),
                        viewBinding.etPassword.getText().toString(),
                        viewBinding.etFirstName.getText().toString(),
                        viewBinding.etLastName.getText().toString(), imageUri);
            }
        });

        viewBinding.btnLogin.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void updateUI() {
        hideDialog();
        MainActivity.startMainActivity(this);
    }

    @Override
    public void authError(Task<AuthResult> task) {
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, Constants.EMAIL_ALREADY_REGISTERED, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
        }
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
            imageUri = data.getData();
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                viewBinding.ivUserImage.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}