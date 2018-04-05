package com.example.teachergradebook.UI.Table;

import com.example.teachergradebook.UI.Base.ActivityScope;
import com.example.teachergradebook.data.TableRepositoryComponent;
import com.example.teachergradebook.util.schedulers.SchedulerModule;

import dagger.Component;

/**
 * Created by Денис on 20.03.2018.
 */

@ActivityScope
@Component(modules = {TablePresenterModule.class, SchedulerModule.class}, dependencies = {
        TableRepositoryComponent.class
})
public interface TableComonent {
    void inject (MainActivity mainActivity);
}
