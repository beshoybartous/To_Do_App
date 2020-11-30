package com.example.todoapp.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.todoapp.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public abstract class BaseActivity<P extends BasePresenter, V extends ViewBinding> extends AppCompatActivity implements BaseView {
    protected P presenter;
    protected V viewBinding;
    protected AlertDialog loadingDialog = null;

    protected abstract V setViewBinding();

    protected abstract P setPresenter();

    protected abstract void onPostCreated();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = setViewBinding();

        setContentView(viewBinding.getRoot());
        presenter = setPresenter();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        loadingDialog = dialogBuilder.setView(inflater.inflate(R.layout.dialog_loading, null)).create();
        loadingDialog.setTitle("Pleas Wait...");
        loadingDialog.setCancelable(false);
        onPostCreated();
    }

    protected void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showDialog() {
        loadingDialog.show();
    }

    protected void hideDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        hideDialog();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}