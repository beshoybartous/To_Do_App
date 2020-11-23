package com.example.todoapp.ui.mainactivity.to_do_list.to_do_page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.R;
import com.example.todoapp.base.BaseActivity;
import com.example.todoapp.databinding.ActivityToDoBinding;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.ToDoModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ToDoActivity extends BaseActivity<ToDoPresenter, ActivityToDoBinding>implements ToDoView {
    private static final String KEY_TO_DO_ITEM = "to do";
    ToDoModel toDoModel;
    public static void startToDoActivity(Context context){
        Intent toDoIntent=new Intent(context,ToDoActivity.class);
        context.startActivity(toDoIntent);
    }
    public static void startToDoActivity(Context context, ToDoModel toDoModel){
        Intent toDoIntent=new Intent(context,ToDoActivity.class);
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
        presenter.getCategories();

        CalendarConstraints.Builder calendarBuilder = new CalendarConstraints.Builder();
        calendarBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        long today=MaterialDatePicker.todayInUtcMilliseconds();
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
        if(getIntent().getSerializableExtra(KEY_TO_DO_ITEM)!=null){
            toDoModel= (ToDoModel) getIntent().getSerializableExtra(KEY_TO_DO_ITEM);
            viewBinding.etTitle.setText(toDoModel.getTitle());
            viewBinding.etDescription.setText(toDoModel.getDescription());
            viewBinding.etDueDate.setText(toDoModel.getDueDate());
            viewBinding.btnSave.setOnClickListener(view -> {
                if(viewBinding.etTitle.getText().toString().equals("")){
                    viewBinding.txtInptTitle.setError("please enter title");
                }
                else{
                    viewBinding.txtInptTitle.setError(null);
                }
                if(viewBinding.etDescription.getText().toString().equals("")){
                    viewBinding.txtInptDescription.setError("please enter description");
                }
                else{
                    viewBinding.txtInptDescription.setError(null);
                }
                if(viewBinding.txtInptDescription.getError()==null &&viewBinding.txtInptTitle.getError()==null){
                    int chipID= viewBinding.chipGroup.getCheckedChipId();
                    Chip chip=findViewById(chipID);
                    CategoryModel categoryModel= (CategoryModel) chip.getTag();
                    presenter.saveToDo(toDoModel.getId(),viewBinding.etTitle.getText().toString(),
                            viewBinding.etDescription.getText().toString(),categoryModel.getId(),
                            toDoModel.isDone(),viewBinding.etDueDate.getText().toString());
                }
            });

        }
        else {
            viewBinding.btnSave.setOnClickListener(view -> {
                viewBinding.txtInptTitle.setError(null);
                viewBinding.txtInptDescription.setError(null);
                viewBinding.txtInptDueDate.setError(null);
                if (viewBinding.etTitle.getText().toString().equals("")) {
                    viewBinding.txtInptTitle.setError("Please enter title");
                }
                if (viewBinding.etDescription.getText().toString().equals("")) {
                    viewBinding.txtInptDescription.setError("Please enter description");
                }
                if(viewBinding.etDueDate.getText().toString().equals("")){
                    viewBinding.txtInptDueDate.setError("Please select date");
                }

                if (viewBinding.txtInptDescription.getError() == null
                        && viewBinding.txtInptTitle.getError() == null
                        && viewBinding.txtInptDueDate.getError() == null) {
                    int chipID= viewBinding.chipGroup.getCheckedChipId();
                    Chip chip=findViewById(chipID);
                    CategoryModel categoryModel= (CategoryModel) chip.getTag();
                    presenter.saveToDo("", viewBinding.etTitle.getText().toString(),
                            viewBinding.etDescription.getText().toString(), categoryModel.getId(),
                            false,viewBinding.etDueDate.getText().toString());
                }
            });
        }
    }

    @Override
    public void getCategoriesView(List<CategoryModel> categoryModelList) {
        boolean isChecked=true;
        for (CategoryModel item:categoryModelList) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, viewBinding.chipGroup, false);
            chip.setText(item.getTitle());
            Drawable drawable=getResources().getDrawable(R.drawable.ic_color);
            drawable.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            chip.setTag(item);
            chip.setChipIcon(drawable );

            viewBinding.chipGroup.addView(chip);
            if(toDoModel!=null){
                Log.d("iamhere", "getCategoriesView:1 ");

                if(viewBinding.chipGroup.getCheckedChipIds().size()==0) {
                    CategoryModel categoryModel = (CategoryModel) chip.getTag();
                    if (categoryModel.getId().equals(toDoModel.getCategoryID())) {
                        viewBinding.chipGroup.check(chip.getId());
                        }
                    }
                }

            else if(isChecked) {
                viewBinding.chipGroup.check(chip.getId());
                isChecked=false;
            }
        }
    }

    @Override
    public void saved(boolean successful) {
        if(successful){
            EventBus.getDefault().postSticky(new MessageEvent());
            finish();
        }
        else{
            Toast.makeText(this, "Failed to save try again later", Toast.LENGTH_SHORT).show();
        }
    }
}