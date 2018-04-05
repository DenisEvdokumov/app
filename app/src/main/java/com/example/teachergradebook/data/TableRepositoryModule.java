package com.example.teachergradebook.data;

import com.example.teachergradebook.data.repository.local.Local;
import com.example.teachergradebook.data.repository.local.LocalTableDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Денис on 06.03.2018.
 */
@Module
public class TableRepositoryModule {

    @Provides
    @Local
    @Singleton
    public LocalTableDataSource provideLocalTableDataSource(LocalTableDataSource localTableDataSource){
        return localTableDataSource;
    }

//    @Provides
//    @Remote
//    @Singleton
//    public TableDataSource provideRemoteDataSource(QuestionRemoteDataSource questionRemoteDataSource) {
//        return questionRemoteDataSource;
//    }


}
