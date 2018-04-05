package com.example.teachergradebook.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.teachergradebook.data.Config;
import com.example.teachergradebook.data.database.StudentGroupDao;
import com.example.teachergradebook.data.database.TeacherGradeDb;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Денис on 15.03.2018.
 */


@Module
public class DataBaseModule {
    private static final String DATABASE = "database_name";

    @Provides
    @Named(DATABASE)
    String provideDatabaseName() {
        return Config.DATABASE_NAME;
    }

    @Provides
    @Singleton
    TeacherGradeDb provideTeacherGradeDao(Context context, @Named(DATABASE) String databaseName) {
        return Room.databaseBuilder(context, TeacherGradeDb.class, databaseName).build();
    }

    @Provides
    @Singleton
    StudentGroupDao provideStudentGroupDao(TeacherGradeDb teacherGradeDb) {
        return teacherGradeDb.studentGroupDao();
    }
}