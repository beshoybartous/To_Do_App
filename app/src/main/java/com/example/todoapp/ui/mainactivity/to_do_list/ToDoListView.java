package com.example.todoapp.ui.mainactivity.to_do_list;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.ToDoModel;

import java.util.List;

public interface ToDoListView extends BaseView {

    void getToDoList(List<ToDoModel> toDoModelList, boolean isDone);

}
