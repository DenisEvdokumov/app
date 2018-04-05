package com.example.teachergradebook.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.teachergradebook.data.Config;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Денис on 12.03.2018.
 */

@Dao
public interface StudentGroupDao {
    @Query("SELECT * FROM " + Config.GROUP_TABLE_NAME)
    Flowable<List<StudentGroup>> getAllStudentGroup();

    @Query("SELECT * FROM " + Config.PRACTICE_TABLE_NAME)
    Flowable <List<Practice>> getAllPractice();


    @Query("SELECT * FROM " + Config.GROUP_TABLE_NAME + " WHERE id == :id")
    Flowable<StudentGroup> getStudentGroupById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StudentGroup studentGroup);

    @Query("DELETE FROM " + Config.STUDENT_TABLE_NAME)
    void deleteAllStudent();

    @Query("SELECT * FROM " + Config.STUDENT_TABLE_NAME)
    Flowable<List<Student>> getAllStudent();

    @Query("SELECT * FROM " + Config.STUDENT_TABLE_NAME + " WHERE id == :id")
    Flowable<Student> getStudentById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Query("DELETE FROM " + Config.GRADE_TABLE_NAME)
    void deleteAllStudentGroup();

    @Query("SELECT * FROM " + Config.COURSE_TABLE_NAME)
    Flowable<List<Course>> getAllCourse();

    @Query("SELECT * FROM " + Config.GRADE_TABLE_NAME)
    Flowable<List<Grade>> getAllGrade();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Grade grade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Practice practice);



}
