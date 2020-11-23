package com.example.todoapp.ui.mainactivity;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.UserModel;


public interface MainView extends BaseView {

    void getUserInfo(UserModel userModel);

}
