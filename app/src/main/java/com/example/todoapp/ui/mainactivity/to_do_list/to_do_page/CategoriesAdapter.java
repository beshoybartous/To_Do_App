package com.example.todoapp.ui.mainactivity.to_do_list.to_do_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.databinding.ItemChipBinding;
import com.example.todoapp.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.RecyclerViewHolder> {
    Context context;
    List<CategoryModel> categoryModelList=new ArrayList<>();
    String selectedCategoryID="";
    int selectedItemPosition=0;
    public CategoriesAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemChipBinding itemChipBinding=ItemChipBinding.inflate(inflater, parent, false);
        return new RecyclerViewHolder(itemChipBinding);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setBinding(categoryModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
    public void setCategoryModelList(List<CategoryModel> categoryModelList){
        this.categoryModelList=categoryModelList;
        notifyDataSetChanged();
    }
    public void setSelectedCategoryID(String selectedCategoryID){
        this.selectedCategoryID=selectedCategoryID;
    }
    public CategoryModel getSelectedItem(){
        return categoryModelList.get(selectedItemPosition);
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ItemChipBinding binding;
        public RecyclerViewHolder(@NonNull ItemChipBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void setBinding(CategoryModel categoryModel){
            if(binding.chip.isChecked()){
                binding.chip.setChecked(false);
            }
            if(getAdapterPosition()==0 && selectedCategoryID.isEmpty()){
                binding.chip.setChecked(true);
            }
            else if(categoryModel.getId().equals(selectedCategoryID)){
                selectedItemPosition=getAdapterPosition();
                binding.chip.setChecked(true);
            }
            binding.chip.setOnClickListener(view -> {
                setSelectedCategoryID(categoryModel.getId());
                selectedItemPosition=getAdapterPosition();

                notifyDataSetChanged();
            });

            binding.chip.setText(categoryModel.getTitle());
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getResources().getDrawable(R.drawable.ic_color);
            drawable.setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC_IN);
            binding.chip.setTag(categoryModel);
            binding.chip.setChipIcon(drawable);
        }
    }
}
