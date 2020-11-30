package com.example.todoapp.ui.mainactivity.to_do_list;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.ToDoModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Observable;

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
//            Observable aa;
//            CollectionReference ref = db.collection("users").document(userID).collection("todoList");
//            ref.whereEqualTo("done", isDone).get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot doc : task.getResult()) {
//                        ToDoModel toDoModel = ToDoModel.getTodo(doc);
//                        toDoModelList.add(toDoModel);
//                    }
//                    aa
//                    view.getToDoList(toDoModelList, isDone);
//                } else {
//                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
//
//                }
//            });
//            RxFirestore.getCollection(ref.whereEqualTo("done", isDone));
//            Observable<List<DocumentSnapshot>> todo=  Observable.create(emitter -> {
//               // CollectionReference ref = db.collection("users").document(userID).collection("todoList");
//
//            });
                    //ref.whereEqualTo("done", false).get().getResult().getDocuments();

            CollectionReference ref = db.collection("users").document(userID).collection("todoList");
            ref.whereEqualTo("done", isDone).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        ToDoModel toDoModel = ToDoModel.getTodo(doc);
                        toDoModelList.add(toDoModel);
                    }
                    view.getToDoList(toDoModelList, isDone);
                } else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());

                }
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
                } else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }
}
