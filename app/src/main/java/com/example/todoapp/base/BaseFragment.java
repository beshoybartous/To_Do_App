package com.example.todoapp.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.todoapp.R;

public abstract class BaseFragment<P extends BasePresenter,V extends ViewBinding> extends Fragment implements BaseView{
    protected P presenter;
    protected V viewBinding;

    protected abstract V setViewBinding();
    protected abstract P setPresenter();
    protected abstract void onPostCreated();
    protected AlertDialog loadingDialog=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding=setViewBinding();
        presenter=setPresenter();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        loadingDialog=dialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_loading,null)).create();
        loadingDialog.setTitle("Pleas Wait...");

        loadingDialog.setCancelable(false);
        onPostCreated();
        return viewBinding.getRoot();
    }

    protected void showDialog(){
        loadingDialog.show();
    }
    protected void hideDialog(){
        loadingDialog.dismiss();
    }
    @Override
    public void showError(String error) {
        hideDialog();
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}