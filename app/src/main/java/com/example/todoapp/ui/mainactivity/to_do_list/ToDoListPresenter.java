package com.example.todoapp.ui.mainactivity.to_do_list;

import android.util.Log;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.model.UserModel;
import com.example.todoapp.ui.mainactivity.MainView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ToDoListPresenter extends BasePresenter {
    ToDoListView view;

    public ToDoListPresenter(ToDoListView view) {
        super(view);
        this.view = view;
    }

    public void getToDoList(boolean isDone) {
        if (user != null) {
            String userID = user.getUid();
            List<ToDoModel> toDoModelList = new ArrayList<>();
            CollectionReference ref= db.collection("users").document(userID).collection("todoList");
            ref.whereEqualTo("done", isDone).get().addOnCompleteListener(task -> {
                for (DocumentSnapshot doc : task.getResult()) {
                    ToDoModel toDoModel=ToDoModel.getTodo(doc);
                    toDoModelList.add(toDoModel);
                }
                view.getToDoList(toDoModelList,isDone);
            });
        }
    }

    public void update(ToDoModel toDoModel, boolean isDone) {
        if (user != null) {
            String userID = user.getUid();
            db.collection("users").document(userID).collection("todoList").document(toDoModel.getId()).
                    set(toDoModel.toMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    getToDoList(isDone);
                }
            });
        }
    }
}
