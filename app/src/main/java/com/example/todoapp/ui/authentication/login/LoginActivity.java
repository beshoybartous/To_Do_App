package com.example.todoapp.ui.authentication.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityLoginBinding;
import com.example.todoapp.ui.authentication.SignUp.SignUpActivity;
import com.example.todoapp.ui.mainactivity.MainActivity;
import com.example.todoapp.utils.Constants;
import com.example.todoapp.utils.HelperUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginView {

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    public static void startLoginActivity(Context context) {
        Intent loginActivityIntent = new Intent(context, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(loginActivityIntent);
    }

    @Override
    protected ActivityLoginBinding setViewBinding() {
        FacebookSdk.setApplicationId(String.valueOf(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());

        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected LoginPresenter setPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onPostCreated() {



        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = viewBinding.btnFacebookLogin;
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //  handleFacebookAccessToken(loginResult.getAccessToken());
                presenter.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                // Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        viewBinding.btnSignUp.setOnClickListener(view -> {
            SignUpActivity.startSignUpActivity(this);
        });

        viewBinding.btnLogin.setOnClickListener(view -> {
            viewBinding.txtUserName.setError(null);
            viewBinding.txtPassword.setError(null);
            if (!viewBinding.etUserName.getText().toString().isEmpty() && !viewBinding.etPassword.getText().toString().isEmpty()) {
                showDialog();
                presenter.loginUser(this, viewBinding.etUserName.getText().toString(),
                        viewBinding.etPassword.getText().toString());
            } else if (HelperUtil.isEditTextEmpty(viewBinding.etUserName)) {
                viewBinding.txtUserName.setError(Constants.EMPTY_EMAIL);
            } else if (HelperUtil.isEditTextEmpty(viewBinding.etPassword)) {
                viewBinding.txtPassword.setError(Constants.EMPTY_PASSWORD);
            }
        });

        viewBinding.btnGoogleSignIn.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            signIn();
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                presenter.googleLogin(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //  Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void updateUI() {
        hideDialog();
        MainActivity.startMainActivity(this);
    }

    @Override
    public void authError(Task<AuthResult> task) {
        hideDialog();
        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
    }


}