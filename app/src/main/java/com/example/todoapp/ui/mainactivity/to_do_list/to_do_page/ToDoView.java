package com.example.todoapp.ui.mainactivity.to_do_list.to_do_page;

import com.example.todoapp.base.BaseView;
import com.example.todoapp.model.CategoryModel;

import java.util.List;

public interface ToDoView extends BaseView {
    void getCategoriesView(List<CategoryModel> categoryModelList);
    void saved(boolean successful);
}
