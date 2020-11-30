package com.example.todoapp.ui.edit_profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.databinding.ActivityEditProfileBinding;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class EditProfileActivity extends BaseActivity<EditProfilePresenter, ActivityEditProfileBinding> implements EditProfileView {
    private static final String KEY_USER_MODEL ="user" ;
    private static final String KEY_UPDATE_USER ="update" ;
    private static final int PICTURE_ID = 123;
    private static final int CAMERA_PERMISSION_REQUEST = 11;
    UserModel userModel;

    Uri imageUri;

    public static void startEditProfileActivity(Context context, CardView cvUserImage, UserModel userModel){
        Intent editProfileIntent =new Intent(context,EditProfileActivity.class);
        editProfileIntent.putExtra(KEY_USER_MODEL, userModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity)context, cvUserImage, "user_image");
        context.startActivity(editProfileIntent,options.toBundle());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected ActivityEditProfileBinding setViewBinding() {
        return ActivityEditProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected EditProfilePresenter setPresenter() {
        return new EditProfilePresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showBackButton();

        if(getIntent().getSerializableExtra(KEY_USER_MODEL)!=null){
            userModel= (UserModel) getIntent().getSerializableExtra(KEY_USER_MODEL);
            String[]name=userModel.getDisplayName().split(" ");
            imageUri= Uri.parse(userModel.getImageUri());
            viewBinding.etFirstName.setText(name[0]);
            viewBinding.etLastName.setText(name[1]);
            Picasso.get().load( userModel.getImageUri()).into(viewBinding.ivUserImage);
        }
        viewBinding.fabNewImage.setOnClickListener(view -> {
            selectImage();
        });
        viewBinding.btnSave.setOnClickListener(view -> {
            loadingDialog.show();
            userModel.setDisplayName(viewBinding.etFirstName.getText().toString()+" "+viewBinding.etLastName.getText().toString());
            userModel.setImageUri(imageUri.toString());
            presenter.updateUser(userModel);
        });
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
            Bitmap photo;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                viewBinding.ivUserImage.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void updateUI() {
        EventBus.getDefault().postSticky(new MessageEvent(KEY_UPDATE_USER));
        hideDialog();
        finish();
    }
}