package com.example.todoapp.ui.mainactivity.categories_list.category_page;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.google.firebase.firestore.DocumentReference;

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
//                    CollectionReference ref = db.collection("users").document(userID).collection("todoList");
//                    ref.whereEqualTo("categoryID", categoryModel.getId()).get().addOnCompleteListener(task1 -> {
//                        if(task1.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task1.getResult()) {
//                                ToDoModel toDoModel=ToDoModel.getTodo(document);
//                                toDoModel.setCategoryColor(color);
//                                toDoModel.setCategoryTitle( title);
//                                db.collection("users").document(userID).collection("todoList").document(toDoModel.getId()).
//                                        set(toDoModel.toMap());
//                            }
//                        }
//                    });
                    view.saved(true);
                } else {
                    view.saved(false);
                }
            });
        }
    }

}
