package com.example.todoapp.base;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.ui.mainactivity.MainView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DummyPresenter extends BasePresenter {
    MainView view;
    public DummyPresenter(MainView view) {
        super(view);
        this.view=view;
    }
}
