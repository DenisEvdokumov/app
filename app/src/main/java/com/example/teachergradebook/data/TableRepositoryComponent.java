package com.example.teachergradebook.data;

import com.example.teachergradebook.AppModule;
import com.example.teachergradebook.data.repository.ApiServiceModule;
import com.example.teachergradebook.data.repository.TableRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Денис on 06.03.2018.
 */

@Singleton
@Component(modules = { TableRepositoryModule.class,ApiServiceModule.class, AppModule.class,DataBaseModule.class})


public interface TableRepositoryComponent {
    TableRepository provideTableRepository();
}
