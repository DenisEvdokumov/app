package com.example.teachergradebook;

import android.app.Application;

import com.example.teachergradebook.data.DaggerTableRepositoryComponent;
import com.example.teachergradebook.data.TableRepositoryComponent;
//import com.example.teachergradebook.data.repository.TableRepository;

/**
 * Created by Денис on 12.03.2018.
 */

public class AndroidApplication extends Application {
    
    private TableRepositoryComponent tableRepositoryComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        initializeDependencies();
    }

    private void initializeDependencies() {
        tableRepositoryComponent = DaggerTableRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public TableRepositoryComponent getTableRepositoryComponent() {
        return tableRepositoryComponent;

    }
}
