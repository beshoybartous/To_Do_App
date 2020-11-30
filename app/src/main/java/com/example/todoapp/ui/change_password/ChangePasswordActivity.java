package com.example.todoapp.ui.change_password;

import android.content.Context;
import android.content.Intent;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityChangePasswordBinding;
import com.example.todoapp.ui.authentication.login.LoginActivity;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordPresenter, ActivityChangePasswordBinding> implements ChangePasswordView {

    public static void startChangePasswordActivity(Context context) {
        Intent changePasswordIntent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(changePasswordIntent);
    }

    @Override
    protected ActivityChangePasswordBinding setViewBinding() {
        return ActivityChangePasswordBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ChangePasswordPresenter setPresenter() {
        return new ChangePasswordPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showBackButton();
        viewBinding.btnSave.setOnClickListener(view -> {
            if (onDataValidation()) {
                showDialog();
                presenter.changePassword(viewBinding.etOldPassword.getText().toString(),
                        viewBinding.etNewPassword.getText().toString());
            }
        });
    }

    private boolean onDataValidation() {
        viewBinding.txtInputOldPassword.setError(null);
        viewBinding.txtInputNewPassword.setError(null);
        viewBinding.txtInputConfirmPassword.setError(null);

        if (viewBinding.etOldPassword.getText().toString().isEmpty()) {
            viewBinding.txtInputOldPassword.setError("Please enter old password");
        }

        if (viewBinding.etNewPassword.getText().toString().isEmpty()) {
            viewBinding.txtInputNewPassword.setError("Please enter new password");
        }

        if (viewBinding.etConfirmPassword.getText().toString().isEmpty()) {
            viewBinding.txtInputConfirmPassword.setError("Please enter new password");
        }

        if (!viewBinding.etConfirmPassword.getText().toString().equals(viewBinding.etNewPassword.getText().toString())) {
            viewBinding.txtInputConfirmPassword.setError("password doesn't match");
        }

        return viewBinding.etConfirmPassword.getText().toString().equals(viewBinding.etNewPassword.getText().toString()) &&
                !viewBinding.etConfirmPassword.getText().toString().isEmpty() &&
                !viewBinding.etOldPassword.getText().toString().isEmpty();
    }


    @Override
    public void signOut() {
        hideDialog();
        LoginActivity.startLoginActivity(this);
        finish();
    }
}