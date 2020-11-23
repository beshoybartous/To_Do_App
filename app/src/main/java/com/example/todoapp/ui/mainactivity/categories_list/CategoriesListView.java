package com.example.todoapp.ui.mainactivity.categories_list;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.CategoryModel;

import java.util.List;

public interface CategoriesListView extends BaseView {
    void onGetCategoriesList(List<CategoryModel> categoryModelList);
    void updateUI();

}
