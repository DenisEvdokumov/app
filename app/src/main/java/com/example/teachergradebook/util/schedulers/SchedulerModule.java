package com.example.teachergradebook.util.schedulers;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.teachergradebook.util.schedulers.SchedulerType.IO;
import static com.example.teachergradebook.util.schedulers.SchedulerType.UI;

/**
 * Created by Денис on 19.03.2018.
 */

@Module
public class SchedulerModule {

    @Provides
    @RunOn(IO)
    Scheduler provireIo() {return Schedulers.io();}

    @Provides
    @RunOn(UI)
    Scheduler provideUi() {
        return AndroidSchedulers.mainThread();
    }
}
