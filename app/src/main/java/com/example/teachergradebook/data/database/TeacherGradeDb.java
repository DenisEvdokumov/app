package com.example.teachergradebook.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Update;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;

/**
 * Created by Денис on 12.03.2018.
 */

@Database(entities = {StudentGroup.class, Student.class, Course.class, Practice.class, Grade.class, User.class, Predmet.class}, version = 4)
public abstract class TeacherGradeDb extends RoomDatabase {

    public abstract StudentGroupDao studentGroupDao();


}
