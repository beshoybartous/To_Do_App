package com.example.todoapp.cache;

import android.util.Log;

import com.example.todoapp.model.CategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPref {
    private static Map<String, CategoryModel> categoryList = new HashMap<>();

    public static List<CategoryModel> getCategoryListget() {
        return new ArrayList<CategoryModel>(categoryList.values());
    }

    public static void addValue(CategoryModel categoryModel) {
        categoryList.put(categoryModel.getId(), categoryModel);
    }

    public static String  getValue(String categoryID) {
        return categoryList.get(categoryID).getColor();
    }

    public static void removeValue(CategoryModel categoryModel) {
        categoryList.values().remove(categoryModel);
    }

    public static boolean contain(String categoryID) {
        return categoryList.containsKey(categoryID);
    }

    public static boolean isEmpty(){
        return categoryList.isEmpty();
    }

    public static void clear(){
        categoryList.clear();
    }
}
