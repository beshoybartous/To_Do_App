package com.example.todoapp.ui.mainactivity.categories_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import com.example.todoapp.base.BaseFragment;
import com.example.todoapp.databinding.FragmentCategoriesListBinding;
import com.example.todoapp.model.CategoryModel;
import com.example.todoapp.model.MessageEvent;
import com.example.todoapp.ui.mainactivity.categories_list.category_page.CategoryBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter, FragmentCategoriesListBinding> implements CategoriesListView, CategoriesAdapter.CategoryClickListener {
    CategoriesAdapter categoriesAdapter;
    private static final String KEY_CATEGORY_ITEM = "category";
    AlertDialog deleteDialog;
    @Override
    protected FragmentCategoriesListBinding setViewBinding() {
        return FragmentCategoriesListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CategoriesListPresenter setPresenter() {
        return new CategoriesListPresenter(this);
    }

    @Override
    protected void onPostCreated() {
        showDialog();
        deleteDialog=new AlertDialog.Builder(getContext()).create();
        deleteDialog.setTitle("Delete");
        deleteDialog.setMessage("This Category will be deleted.\nDo you want to delete it's todo?");

        categoriesAdapter = new CategoriesAdapter(getContext(), CategoriesListFragment.this);
        viewBinding.rvCategoryList.setAdapter(categoriesAdapter);
        presenter.getCategoriesList();
        viewBinding.fabNewToDo.setOnClickListener(view -> {
            CategoryBottomSheet categoryBottomSheet=new CategoryBottomSheet();
            categoryBottomSheet.show(getParentFragmentManager()  , "tag");
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
        if(event.getMessage().equals("category")) {
            presenter.getCategoriesList();
        }
    }

    @Override
    public void onGetCategoriesList(List<CategoryModel> categoryModelList) {
        Log.d("onGetCategoriesList", "onGetCategoriesList: ");
        categoriesAdapter.updateList(categoryModelList);
        categoriesAdapter.notifyDataSetChanged();
        hideDialog();
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void categoryOnCLick(CategoryModel categoryModel,boolean isDelete) {
        if(isDelete){
            deleteDialog.setButton(Dialog.BUTTON_NEGATIVE, "Delete it's todo", (dialogInterface, i) -> {
                presenter.deleteCategory(categoryModel, true);
            });
            deleteDialog.setButton(Dialog.BUTTON_POSITIVE, "No don't", (dialogInterface, i) -> {
                presenter.deleteCategory(categoryModel, false);

            });
            deleteDialog.setButton(Dialog.BUTTON_NEUTRAL, "Cancel", (dialogInterface, i) -> {
                deleteDialog.dismiss();

            });
            deleteDialog.show();
        }
        else {
            CategoryBottomSheet categoryBottomSheet = new CategoryBottomSheet();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_CATEGORY_ITEM, categoryModel);
            categoryBottomSheet.setArguments(bundle);
            categoryBottomSheet.show(getParentFragmentManager(), "tag");
        }
    }
}