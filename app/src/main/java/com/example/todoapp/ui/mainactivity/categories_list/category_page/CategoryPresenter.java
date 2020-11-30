package com.example.todoapp.ui.mainactivity.categories_list.category_page;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class CategoryPresenter extends BasePresenter {
    Categoryview view;

    public CategoryPresenter(Categoryview view) {
        super(view);
        this.view = view;
    }

    public void saveCategory(String id, String title, String color) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            if (id == "") {
                DocumentReference ref = db.collection("users").document(userID).collection("categories").document();
                id = ref.getId();
            }
            CategoryModel categoryModel = new CategoryModel(
                    id,
                    title,
                    color);
            db.collection("users").document(userID).collection("categories").document(id).set(categoryModel.toMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SharedPref.addValue(categoryModel);
                    view.saved(true);
                } else {
                    view.showError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }

}
