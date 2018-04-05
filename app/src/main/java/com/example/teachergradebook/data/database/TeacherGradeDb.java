package com.example.teachergradebook.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;

/**
 * Created by Денис on 12.03.2018.
 */

@Database(entities = {StudentGroup.class, Student.class, Course.class, Practice.class, Grade.class}, version = 1)
public abstract class TeacherGradeDb extends RoomDatabase {

    public abstract StudentGroupDao studentGroupDao();


}
