package com.example.todoapp.ui.mainactivity.to_do_list.to_do_page;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.ToDoModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToDoPresenter extends BasePresenter {
    ToDoView view;

    public ToDoPresenter(ToDoView view) {
        super(view);
        this.view = view;
    }

    public void getCategories() {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            List<CategoryModel> categoryModelList = new ArrayList<>();
            db.collection("users").document(userID).collection("categories").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        categoryModelList.add(CategoryModel.getCategory(doc));
                    }
                    view.getCategoriesView(categoryModelList);
                } else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }

    public void saveToDo(String id, String title, String description, String categoryID, boolean isDone, String etDueDate) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            if (id == "") {
                DocumentReference ref = db.collection("users").document(userID).collection("todoList").document();
                id = ref.getId();
            }
            ToDoModel toDoModel = new ToDoModel(
                    id,
                    title,
                    description,
                    isDone,
                    categoryID,
                    etDueDate);
            db.collection("users").document(userID).collection("todoList").document(id).set(toDoModel.toMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    view.saved();
                } else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }

}
