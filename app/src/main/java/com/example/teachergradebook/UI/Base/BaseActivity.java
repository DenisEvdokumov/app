package com.example.teachergradebook.UI.Base;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;

import com.example.teachergradebook.AndroidApplication;
import com.example.teachergradebook.data.TableRepositoryComponent;

import javax.annotation.Nullable;
//import com.example.teachergradebook.data.repository.TableRepository;

/**
 * Created by Денис on 06.03.2018.
 */

public abstract class BaseActivity <V extends BaseContract.View, P extends BaseContract.Presenter<V>>
        extends AppCompatActivity implements BaseContract.View, LifecycleRegistryOwner {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    protected P presenter;
    protected TableRepositoryComponent getTableRepositoryComponent() {
        return ((AndroidApplication) getApplication()).getTableRepositoryComponent();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseViewModel<V, P> viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        boolean isPresenterCreated = false;
        if (viewModel.getPresenter() == null) {
            viewModel.setPresenter(initPresenter());
            isPresenterCreated = true;
        }
        presenter = viewModel.getPresenter();


    }

    protected abstract P initPresenter();

}