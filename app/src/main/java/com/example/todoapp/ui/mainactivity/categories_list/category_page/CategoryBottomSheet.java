package com.example.todoapp.ui.mainactivity.categories_list.category_page;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.base.BaseBottomSheet;
import com.example.todoapp.databinding.BottomSheetCategoryBinding;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.utils.Constants;
import com.example.todoapp.utils.HelperUtil;

import org.greenrobot.eventbus.EventBus;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CategoryBottomSheet extends BaseBottomSheet<CategoryPresenter, BottomSheetCategoryBinding> implements Categoryview {
    private static final String KEY_CATEGORY_ITEM = "category";
    CategoryModel categoryModel;
    String hexColor="#000000";
    int defaultColor;

    @Override
    protected BottomSheetCategoryBinding setViewBinding() {
        return BottomSheetCategoryBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CategoryPresenter setPresenter() {
        return new CategoryPresenter(this);
    }

    @Override
    protected void onPostCreated() {

        viewBinding.btnChangeColor.setOnClickListener(view -> {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                   // viewBinding.btnChangeColor.setTextColor(color);
                    defaultColor = color;
                    hexColor = String.format("#%06X", (0xFFFFFF & defaultColor));
                   // categoryModel.setColor(hexColor);
                    Drawable drawable=getResources().getDrawable(R.drawable.ic_color_selector);
                    drawable.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
                    viewBinding.btnChangeColor.setImageDrawable(drawable);
                }
            });
            colorPicker.show();
        });

        if(getArguments()!=null){
            viewBinding.tvTitle.setText("Edit category");
            categoryModel= (CategoryModel) getArguments().getSerializable(KEY_CATEGORY_ITEM);
            viewBinding.etTitle.setText(categoryModel.getTitle());
            Log.d("coloris", categoryModel.getColor());
            defaultColor= Color.parseColor(categoryModel.getColor());
            Drawable drawable=getResources().getDrawable(R.drawable.ic_color_selector);
            drawable.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
            viewBinding.btnChangeColor.setImageDrawable(drawable);
            viewBinding.btnSave.setOnClickListener(view -> {
             //   showDialog();
                presenter.saveCategory(categoryModel.getId(), viewBinding.etTitle.getText().toString(), hexColor);
            });
        }
        else{
            viewBinding.tvTitle.setText("Add new category");

            viewBinding.btnSave.setOnClickListener(view -> {
                //   showDialog();
                if(!HelperUtil.isEditTextEmpty(viewBinding.etTitle)) {
                    presenter.saveCategory("", viewBinding.etTitle.getText().toString(), hexColor);
                }
                else{
                    viewBinding.txtInptTitle.setError(Constants.EMPTY_TITLE);
                }
            });
        }
    }

    @Override
    public void saved(boolean successful) {
        // hideDialog();
        if (successful) {
            EventBus.getDefault().postSticky(new MessageEvent("category"));
            dismiss();
            //  finish();
        } else {
            Toast.makeText(getContext(), "Failed to save try again later", Toast.LENGTH_SHORT).show();
        }
    }

}