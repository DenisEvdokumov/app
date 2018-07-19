package com.example.teachergradebook.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.teachergradebook.data.Config;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Денис on 12.03.2018.
 */

@Dao
public interface StudentGroupDao {
    @Query("SELECT * FROM " + Config.GROUP_TABLE_NAME)
    Single<List<StudentGroup>> getAllStudentGroup();

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

    @Query("SELECT * FROM " + Config.GRADE_TABLE_NAME + " WHERE studentId == :studentId " + "AND" + " practiceId == :practiceId")
    Flowable<Grade> getGradeById(int studentId,long practiceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Query("DELETE FROM " + Config.GRADE_TABLE_NAME)
    void deleteAllStudentGroup();

    @Query("SELECT * FROM " + Config.COURSE_TABLE_NAME)
    Flowable<List<Course>> getAllCourse();

    @Query("SELECT * FROM " + Config.GRADE_TABLE_NAME +  " WHERE practiceId == :courseId " + "AND" + " studentId == :courseId")
    Flowable<List<Grade>> getAllGrade(String courseId,String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Grade grade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Practice practice);

    @Update
    void update(Grade grade);

    @Query("SELECT * FROM " + Config.USER_TABLE_NAME + " WHERE login == :login " + "AND" + " password == :password")
    Single<User> login(String login, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveUser(User user);

    @Query("SELECT * FROM " + Config.PREDMET_TABLE_NAME + " WHERE userId == :userId ")
    Single<List<Predmet>> getAllPredmet(Long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPredmet(List<Predmet> predmets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStudentGroup(ArrayList<StudentGroup> studentGroupREsp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveStudent(List<Student> listStudent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveGrade(List<Grade> listGrade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePractice(List<Practice> listPractice);
}
