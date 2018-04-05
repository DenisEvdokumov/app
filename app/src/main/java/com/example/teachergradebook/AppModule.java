package com.example.teachergradebook;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Денис on 12.03.2018.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule (Application context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }
}
