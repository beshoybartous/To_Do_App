package com.example.todoapp.ui.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.todoapp.R;
import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.databinding.CircularImageBinding;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements MainView {

    public static void startMainActivity(Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(mainActivityIntent);
    }

    @Override
    protected ActivityMainBinding setViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected MainPresenter setPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        NavController navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        NavigationUI.setupWithNavController(viewBinding.bottomNav, navController);
        viewBinding.bottomNav.setBackground(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventData(MessageEvent event) {
        if (event.getMessage() != null) {
            presenter.getUserInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        presenter.getUserInfo();
        return true;
    }

    @Override
    public void getUserInfo(UserModel userModel) {
        CircularImageBinding circularImageBinding = CircularImageBinding.bind(this.findViewById(R.id.cl_user_image));
        circularImageBinding.ivUserImage.setOnClickListener(view -> {
            ProfileActivity.startProfileActivity(this, userModel);
        });
        Picasso.get().load(userModel.getImageUri()).placeholder(R.drawable.ic_user_place_holder).into(circularImageBinding.ivUserImage);
    }
}