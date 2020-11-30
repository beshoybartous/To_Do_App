package com.example.todoapp.ui.authentication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityLoginBinding;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.model.type.AccountType;
import com.example.todoapp.ui.authentication.SignUp.SignUpActivity;
import com.example.todoapp.ui.mainactivity.MainActivity;
import com.example.todoapp.utils.Constants;
import com.example.todoapp.utils.HelperUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;

public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginView {

    private static final int RC_SIGN_IN = 123;
    private static final String EMAIL = "email";
    GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    public static void startLoginActivity(Context context) {
        Intent loginActivityIntent = new Intent(context, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(loginActivityIntent);
    }

    @Override
    protected ActivityLoginBinding setViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected LoginPresenter setPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        viewBinding.btnLogin.setOnClickListener(view -> {
            showDialog();
            onEmailLoginValidation();
        });

        viewBinding.btnFacebookLogin.setOnClickListener(view -> {
            showDialog();
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(EMAIL));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    onLoginWithFacebook(loginResult);
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
        });

        viewBinding.btnGoogleSignIn.setOnClickListener(view -> {
            showDialog();
            onLoginWithGoogle();
        });

        viewBinding.btnSignUp.setOnClickListener(view -> {
            SignUpActivity.startSignUpActivity(this,null);
        });
    }

    private void onEmailLoginValidation() {
        viewBinding.txtUserName.setError(null);
        viewBinding.txtPassword.setError(null);
        if (!viewBinding.etUserName.getText().toString().isEmpty() && !viewBinding.etPassword.getText().toString().isEmpty()) {
            showDialog();
            presenter.emailPasswordLogin(this, viewBinding.etUserName.getText().toString(),
                    viewBinding.etPassword.getText().toString());
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etUserName)) {
            viewBinding.txtUserName.setError(Constants.EMPTY_EMAIL);
        } else if (HelperUtil.isEditTextEmpty(viewBinding.etPassword)) {
            viewBinding.txtPassword.setError(Constants.EMPTY_PASSWORD);
        }
    }

    private void onLoginWithFacebook(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            String email = null;
            if (response.getJSONObject().has("email")) {
                try {
                    email = response.getJSONObject().getString("email");
                    UserModel userModel = new UserModel(AccountType.FACEBOOK, email, loginResult.getAccessToken().getToken());
                    Profile profile = Profile.getCurrentProfile();
                    userModel.setImageUri(String.valueOf(profile.getProfilePictureUri(72, 72)));
                    userModel.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
                    presenter.isEmailExist(userModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,picture.type(large),gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void onLoginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.d("calledface", String.valueOf(resultCode));
        if(resultCode== Activity.RESULT_CANCELED){
            hideDialog();
        }
        else {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    account = task.getResult(ApiException.class);
                    UserModel userModel = new UserModel(AccountType.GOOGLE, account.getEmail(), account.getIdToken());
                    userModel.setImageUri(String.valueOf(account.getPhotoUrl()));
                    userModel.setDisplayName(account.getDisplayName());
                    presenter.isEmailExist(userModel);
                } catch (ApiException e) {
                }
            } else {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void updateUI(boolean isNewAccount, UserModel userModel) {
        hideDialog();
        if (isNewAccount) {
            SignUpActivity.startSignUpActivity(this, userModel);
        } else {
            MainActivity.startMainActivity(this);
        }
    }


    @Override
    public void userAlreadyExist() {
        hideDialog();
        Toast.makeText(this, "this account already registered", Toast.LENGTH_LONG).show();

    }


}