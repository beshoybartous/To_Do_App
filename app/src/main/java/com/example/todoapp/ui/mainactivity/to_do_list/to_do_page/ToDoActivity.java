package com.example.todoapp.ui.mainactivity.to_do_list.to_do_page;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityToDoBinding;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.model.ToDoModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ToDoActivity extends BaseActivity<ToDoPresenter, ActivityToDoBinding> implements ToDoView {
    private static final String KEY_TO_DO_ITEM = "to do";
    ToDoModel toDoModel;
    CategoriesAdapter categoriesAdapter;

    public static void startToDoActivity(Context context) {
        Intent toDoIntent = new Intent(context, ToDoActivity.class);
        context.startActivity(toDoIntent);
    }

    public static void startToDoActivity(Context context, ToDoModel toDoModel) {
        Intent toDoIntent = new Intent(context, ToDoActivity.class);
        toDoIntent.putExtra(KEY_TO_DO_ITEM, toDoModel);
        context.startActivity(toDoIntent);
    }

    @Override
    protected ActivityToDoBinding setViewBinding() {
        return ActivityToDoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ToDoPresenter setPresenter() {
        return new ToDoPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showBackButton();
        categoriesAdapter = new CategoriesAdapter(this);
        showBackButton();
        viewBinding.rvCategory.setAdapter(categoriesAdapter);
        presenter.getCategories();
        onDatePickerCreate();

        if (getIntent().getSerializableExtra(KEY_TO_DO_ITEM) != null) {
            toDoModel = (ToDoModel) getIntent().getSerializableExtra(KEY_TO_DO_ITEM);
            viewBinding.etTitle.setText(toDoModel.getTitle());
            viewBinding.etDescription.setText(toDoModel.getDescription());
            viewBinding.etDueDate.setText(toDoModel.getDueDate());
        }

        viewBinding.btnSave.setOnClickListener(view -> {
            if (onDataValidation()) {
                CategoryModel categoryModel = categoriesAdapter.getSelectedItem();
                String id = "";
                if (toDoModel != null) {
                    id = toDoModel.getId();
                }
                presenter.saveToDo(id, viewBinding.etTitle.getText().toString(),
                        viewBinding.etDescription.getText().toString(), categoryModel.getId(),
                        false, viewBinding.etDueDate.getText().toString());
            }
        });
    }

    private void onDatePickerCreate() {
        CalendarConstraints.Builder calendarBuilder = new CalendarConstraints.Builder();
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        calendarBuilder.setValidator(DateValidatorPointForward.now());
        builder.setSelection(today);
        builder.setTitleText("Select a date");
        builder.setCalendarConstraints(calendarBuilder.build());
        MaterialDatePicker materialDatePicker = builder.build();

        viewBinding.etDueDate.setOnClickListener(view -> {
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            viewBinding.etDueDate.setText(materialDatePicker.getHeaderText());
        });
    }

    private boolean onDataValidation() {
        viewBinding.txtInptTitle.setError(null);
        viewBinding.txtInptDescription.setError(null);
        viewBinding.txtInptDueDate.setError(null);
        if (viewBinding.etTitle.getText().toString().equals("")) {
            viewBinding.txtInptTitle.setError("Please enter title");
        }
        if (viewBinding.etDescription.getText().toString().equals("")) {
            viewBinding.txtInptDescription.setError("Please enter description");
        }
        if (viewBinding.etDueDate.getText().toString().equals("")) {
            viewBinding.txtInptDueDate.setError("Please select date");
        }

        return viewBinding.txtInptDescription.getError() == null
                && viewBinding.txtInptTitle.getError() == null
                && viewBinding.txtInptDueDate.getError() == null;
    }

    @Override
    public void getCategoriesView(List<CategoryModel> categoryModelList) {
        if (toDoModel != null) {
            categoriesAdapter.setSelectedCategoryID(toDoModel.getCategoryID());
        }
        categoriesAdapter.setCategoryModelList(categoryModelList);

    }

    @Override
    public void saved() {
            EventBus.getDefault().postSticky(new MessageEvent());
            finish();
    }
}