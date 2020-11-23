package com.example.todoapp.ui.mainactivity.categories_list;

import com.example.todoapp.base.BasePresenter;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.model.CategoryModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class CategoriesListPresenter extends BasePresenter {
    CategoriesListView view;

    public CategoriesListPresenter(CategoriesListView view) {
        super(view);
        this.view = view;
    }

    public void getCategoriesList() {
//        if (user != null) {
//            String userID = user.getUid();
//            List<CategoryModel> categoryModelList = new ArrayList<>();
//            db.collection("users").document(userID).collection("categories").get().addOnCompleteListener(task -> {
//                for (DocumentSnapshot doc : task.getResult()) {
//                    categoryModelList.add(CategoryModel.getCategory(doc));
//                }
//                view.onGetCategoriesList(categoryModelList);
//            });
//        }
        view.onGetCategoriesList(SharedPref.getCategoryListget());
    }

    public void deleteCategory(CategoryModel categoryModel, boolean deleteRelatedToDoS) {
        if (user != null) {
            String userID = user.getUid();
            db.collection("users").document(userID).collection("categories").document(categoryModel.getId()).delete().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    SharedPref.removeValue(categoryModel);
                    getCategoriesList();
                    if (deleteRelatedToDoS) {
                        CollectionReference ref = db.collection("users").document(userID).collection("todoList");
                        ref.whereEqualTo("categoryID", categoryModel.getId()).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (DocumentSnapshot document : task1.getResult()) {
                                    if (deleteRelatedToDoS) {
                                        ref.document(document.getId()).delete();
                                    }
                                }
                            }
                        });
                    }
//                    CollectionReference ref = db.collection("users").document(userID).collection("todoList");
//                    ref.whereEqualTo("categoryID", categoryModel.getId()).get().addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful()) {
//                            for (DocumentSnapshot document : task1.getResult()) {
//                                if (deleteRelatedToDoS) {
//                                    ref.document(document.getId()).delete();
//                                } else {
//                                    ref.document(document.getId()).get().addOnCompleteListener(task2 -> {
//                                        if (task2.isSuccessful()) {
//                                            ToDoModel toDoModel = ToDoModel.getTodo(task2.getResult());
//                                            toDoModel.setCategoryTitle("No Category");
//                                            toDoModel.setCategoryColor("#000000");
//                                            ref.document(toDoModel.getId()).set(toDoModel);
//                                        }
//                                    });
//                                }
//                            }
//                            getCategoriesList();
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    });
                }
            });

        }
    }

    public void update(CategoryModel categoryModel) {
        if (user != null) {
            String userID = user.getUid();
            db.collection("users").document(userID).collection("categories").document(categoryModel.getId()).
                    set(categoryModel.toMap()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    getCategoriesList();
                }
            });
        }
    }

}
