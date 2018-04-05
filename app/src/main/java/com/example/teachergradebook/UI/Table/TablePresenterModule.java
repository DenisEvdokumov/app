package com.example.teachergradebook.UI.Table;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Денис on 20.03.2018.
 */

@Module
public class TablePresenterModule {
    private TableContract.View view;

    public TablePresenterModule(TableContract.View view){
        this.view = view;
    }

    @Provides
    public TableContract.View provideView() { return view; }
}
