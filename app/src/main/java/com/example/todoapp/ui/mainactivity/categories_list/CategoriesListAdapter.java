package com.example.todoapp.ui.mainactivity.categories_list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.databinding.ItemCategoryBinding;
import com.example.todoapp.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.RecyclerViewHolder> {
    protected Context context;
    List<CategoryModel> categoryModelList=new ArrayList<>();
    final CategoryClickListener categoryClickListener;

    public CategoriesListAdapter(Context context, CategoryClickListener categoryClickListener){
        this.context=context;
        this.categoryClickListener=categoryClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemCategoryBinding itemCategoryBinding=ItemCategoryBinding.inflate(inflater, parent, false);
        return new RecyclerViewHolder(itemCategoryBinding);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setBinding(categoryModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public void updateList(List<CategoryModel> list){
        categoryModelList= list;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemCategoryBinding binding;
        public RecyclerViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        protected void setBinding(CategoryModel categoryModel){
            binding.getRoot().setOnClickListener(this);
            Drawable drawable= binding.ivCategoryColor.getDrawable();
            drawable.setColorFilter(Color.parseColor(categoryModel.getColor()), PorterDuff.Mode.SRC_IN);
            binding.ivCategoryColor.setImageDrawable(drawable);
            binding.tvTitle.setText(categoryModel.getTitle());
            binding.btnDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(view.getId()==R.id.btn_delete){
                categoryClickListener.categoryOnCLick(categoryModelList.get(position),true);
            }
            else{
                categoryClickListener.categoryOnCLick(categoryModelList.get(position),false);
            }

        }
    }

    public interface CategoryClickListener{
        void categoryOnCLick(CategoryModel categoryModel,boolean isDelete);
    }
}
