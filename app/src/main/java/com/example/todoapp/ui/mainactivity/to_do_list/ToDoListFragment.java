package com.example.todoapp.ui.mainactivity.to_do_list;

import android.view.View;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.base.BaseFragment;
import com.example.todoapp.cache.SharedPref;
import com.example.todoapp.databinding.FragmentToDoListBinding;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.ui.mainactivity.to_do_list.to_do_page.ToDoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ToDoListFragment extends BaseFragment<ToDoListPresenter, FragmentToDoListBinding> implements ToDoListView, ToDoListAdapter.ToDoClickListener {
    ToDoListAdapter toDoListAdapter,finishedToDoListAdapter;
    boolean isExpandedToDo=true,isExpandedFinishedToDo=true;
    @Override
    protected FragmentToDoListBinding setViewBinding() {
        return FragmentToDoListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ToDoListPresenter setPresenter() {
        return new ToDoListPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showDialog();
        toDoListAdapter = new ToDoListAdapter(getContext(), ToDoListFragment.this);
        viewBinding.rvToDoList.setAdapter(toDoListAdapter);
        finishedToDoListAdapter = new ToDoListAdapter(getContext(), ToDoListFragment.this);
        viewBinding.rvFinishedToDoList.setAdapter(finishedToDoListAdapter);
        presenter.getToDoList(false);
        presenter.getToDoList(true);
        viewBinding.fabNewToDo.setOnClickListener(view -> {
            if(SharedPref.isEmpty()){
                Toast.makeText(getContext(), "Add one category atleast", Toast.LENGTH_LONG).show();
            }
            else {
                ToDoActivity.startToDoActivity(getContext());
            }
        });
        viewBinding.clFinishedToDo.setOnClickListener(view -> {
            if(isExpandedFinishedToDo) {
                isExpandedFinishedToDo=false;
                viewBinding.ivExpandFinishedToDo.setImageResource(R.drawable.ic_arrow_down);
                viewBinding.rvFinishedToDoList.setVisibility(View.GONE);
            }
            else{
                isExpandedFinishedToDo=true;
                viewBinding.ivExpandFinishedToDo.setImageResource(R.drawable.ic_arrow_up);
                viewBinding.rvFinishedToDoList.setVisibility(View.VISIBLE);
            }
        });
        viewBinding.clToDoList.setOnClickListener(view -> {
            if(isExpandedToDo) {
                isExpandedToDo=false;
                viewBinding.ivExpandToDo.setImageResource(R.drawable.ic_arrow_down);
                viewBinding.rvToDoList.setVisibility(View.GONE);
            }
            else{
                isExpandedToDo=true;
                viewBinding.ivExpandToDo.setImageResource(R.drawable.ic_arrow_up);
                viewBinding.rvToDoList.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventData(MessageEvent event) {
        if(event.getMessage()==null) {
            presenter.getToDoList(false);
            presenter.getToDoList(true);
        }
    }

    @Override
    public void getToDoList(List<ToDoModel> toDoModelList, boolean isDone) {
        if(isDone){
            finishedToDoListAdapter.updateList(toDoModelList);
            finishedToDoListAdapter.notifyDataSetChanged();
        }
        else {
            toDoListAdapter.updateList(toDoModelList);
            toDoListAdapter.notifyDataSetChanged();
        }

        if(!finishedToDoListAdapter.isEmpty()||!toDoListAdapter.isEmpty()){
            viewBinding.ivNoItem.setVisibility(View.GONE);
            viewBinding.llListView.setVisibility(View.VISIBLE);
            if (finishedToDoListAdapter.isEmpty()) {
                viewBinding.clFinishedToDo.setVisibility(View.GONE);
            } else {
                viewBinding.clFinishedToDo.setVisibility(View.VISIBLE);
            }
            if (toDoListAdapter.isEmpty()) {
                viewBinding.clToDoList.setVisibility(View.GONE);
            } else {
                viewBinding.clToDoList.setVisibility(View.VISIBLE);
            }
        }
        else {
            viewBinding.ivNoItem.setVisibility(View.VISIBLE);
            viewBinding.llListView.setVisibility(View.GONE);
        }
        hideDialog();
    }



    @Override
    public void toDoOnCLick(ToDoModel toDoModel) {
        ToDoActivity.startToDoActivity(getContext(), toDoModel);
    }

    @Override
    public void isDone(ToDoModel toDoModel, boolean isDone) {
        presenter.update(toDoModel,isDone);
    }


}