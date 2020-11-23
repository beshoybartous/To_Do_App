package com.example.todoapp.utils;

import android.widget.EditText;

public class HelperUtil {

    public static boolean isEditTextEmpty(EditText editText){
        return editText.getText().toString().isEmpty();
    }
}
