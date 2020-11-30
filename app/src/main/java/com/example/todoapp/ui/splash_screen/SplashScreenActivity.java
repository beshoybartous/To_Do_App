package com.example.todoapp.ui.splash_screen;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivitySplashScreenBinding;
import com.example.todoapp.ui.authentication.login.LoginActivity;
import com.example.todoapp.ui.mainactivity.MainActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreenActivity extends BaseActivity<SplashScreenPresenter, ActivitySplashScreenBinding> implements SplashScreenView {


    @Override
    protected ActivitySplashScreenBinding setViewBinding() {
        return ActivitySplashScreenBinding.inflate(getLayoutInflater());
    }

    @Override
    protected SplashScreenPresenter setPresenter() {
        return new SplashScreenPresenter(this);
    }

    @Override
    protected void onPostCreated() {
//        try {
//            PackageInfo packageInfo=getPackageManager().getPackageInfo("com.example.todoapp", PackageManager.GET_SIGNATURES);
//            for(Signature signature:packageInfo.signatures){
//                MessageDigest md=MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("mykeyyy", Base64.encodeToString(md.digest(),Base64.DEFAULT));
//
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        presenter.checkLoggedIn();
    }

    @Override
    public void isLoggedIn(boolean isLogged) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogged) {
                    MainActivity.startMainActivity(SplashScreenActivity.this);
                } else {
                    LoginActivity.startLoginActivity(SplashScreenActivity.this);
                }
            }
        }, 3000);

    }
}