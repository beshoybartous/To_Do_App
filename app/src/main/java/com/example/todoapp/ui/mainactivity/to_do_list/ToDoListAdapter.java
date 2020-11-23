package com.example.todoapp.ui.mainactivity.to_do_list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.databinding.ItemToDoBinding;
import com.example.todoapp.model.ToDoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.RecyclerViewHolder> {
    protected Context context;
    List<ToDoModel> toDoModelList=new ArrayList<>();
    final ToDoClickListener toDoClickListener;
    public ToDoListAdapter(Context context, ToDoClickListener toDoClickListener){
        this.context=context;
        this.toDoClickListener=toDoClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemToDoBinding itemToDoBinding=ItemToDoBinding.inflate(inflater, parent, false);
        return new RecyclerViewHolder(itemToDoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setBinding(toDoModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    public void updateList(List<ToDoModel> list){
        toDoModelList= list;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemToDoBinding binding;
        public RecyclerViewHolder(ItemToDoBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        protected void setBinding(ToDoModel toDoModel){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String getCurrentDateTime = sdf.format(calendar.getTime());

            binding.cbDone.setChecked(toDoModel.isDone());
            if(toDoModel.isDone()) {
                binding.tvTitle.setPaintFlags(binding.tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (getCurrentDateTime.compareTo(toDoModel.getDueDate()) > 0 && !toDoModel.isDone())
            {
                binding.tvTitle.setTextColor(Color.RED);
                binding.tvDueDate.setTextColor(Color.RED);
                binding.tvDescription.setTextColor(Color.RED);
            }
            else{
                binding.tvTitle.setTextColor(Color.BLACK);
                binding.tvDueDate.setTextColor(Color.BLACK);
                binding.tvDescription.setTextColor(Color.BLACK);
            }
            if(!SharedPref.contain(toDoModel.getCategoryID())){
                binding.vCategoryColor.setBackgroundColor(Color.parseColor("#000000"));
            }
            else{
                String color=SharedPref.getValue(toDoModel.getCategoryID());
                binding.vCategoryColor.setBackgroundColor(Color.parseColor(color));
            }
            binding.tvDueDate.setText(toDoModel.getDueDate());
            binding.tvTitle.setText(toDoModel.getTitle());
            binding.tvDescription.setText(toDoModel.getDescription());
            binding.getRoot().setOnClickListener(this);
            binding.cbDone.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(view.getId()== R.id.cb_done){
                toDoModelList.get(position).setDone(binding.cbDone.isChecked());
                toDoClickListener.isDone(toDoModelList.get(position),binding.cbDone.isChecked());
                if(binding.cbDone.isChecked()) {
                    binding.tvTitle.setPaintFlags(binding.tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    binding.tvTitle.setPaintFlags(0);
                }
                if(binding.cbDone.isChecked()){
                    toDoModelList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
                else{
                    toDoModelList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            }
            else {
                toDoClickListener.toDoOnCLick(toDoModelList.get(position));
            }
        }
    }

    public interface ToDoClickListener{
        void toDoOnCLick(ToDoModel toDoModel);
        void isDone(ToDoModel toDoModel, boolean checked);
    }
}
