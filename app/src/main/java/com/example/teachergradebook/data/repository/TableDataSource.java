package com.example.teachergradebook.data.repository;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Денис on 14.03.2018.
 */

public interface TableDataSource {
    Flowable<List<Student>> loadStudents(boolean forceRemote);
    Flowable<List<StudentGroup>> loadStudentGroups(boolean forceRemote);
    Flowable<List<Grade>> loadGrade(boolean forceRemote);
    Flowable<List<Practice>> loadPractice(boolean forceRemote);
    Flowable<List<Course>> loadCourse(boolean forceRemote);

    void addGroup(StudentGroup studentGroup);
    Completable addStudent(Student student);
    void addGrade(Grade grade);
    Completable addPractice(Practice practice);
    void addCourse(Course course);


    void clearData();
}
