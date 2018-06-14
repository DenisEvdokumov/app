package com.example.teachergradebook.UI.PractieGroupList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Денис on 20.03.2018.
 */

@Module
public class PGPresenterModule {
    private PGContract.View view;

    public PGPresenterModule(PGContract.View view){
        this.view = view;
    }

    @Provides
    public PGContract.View provideView() { return view; }
}
