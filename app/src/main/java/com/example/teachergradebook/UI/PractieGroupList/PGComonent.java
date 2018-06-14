package com.example.teachergradebook.UI.PractieGroupList;

import com.example.teachergradebook.UI.Base.ActivityScope;
import com.example.teachergradebook.data.TableRepositoryComponent;
import com.example.teachergradebook.util.schedulers.SchedulerModule;

import dagger.Component;

/**
 * Created by Денис on 20.03.2018.
 */

@ActivityScope
@Component(modules = {PGPresenterModule.class, SchedulerModule.class}, dependencies = {
        TableRepositoryComponent.class
})
public interface PGComonent {
    void inject(PGActivity mainActivity);
}
