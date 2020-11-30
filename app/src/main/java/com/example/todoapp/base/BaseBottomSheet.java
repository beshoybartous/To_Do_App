package com.example.todoapp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.example.todoapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheet<P extends BasePresenter, V extends ViewBinding> extends BottomSheetDialogFragment implements BaseView {
    protected P presenter;
    protected V viewBinding;

    protected abstract V setViewBinding();

    protected abstract P setPresenter();

    protected abstract void onPostCreated();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = setViewBinding();
        presenter = setPresenter();
        onPostCreated();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return viewBinding.getRoot();
    }
    @Override
    public void showError(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
    @Override public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
