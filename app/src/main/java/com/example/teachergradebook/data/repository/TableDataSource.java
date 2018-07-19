package com.example.teachergradebook.data.repository;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.Res;
import com.example.teachergradebook.data.model.ResList;
import com.example.teachergradebook.data.model.ResList1;
import com.example.teachergradebook.data.model.ResList2;
import com.example.teachergradebook.data.model.ResList3;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Денис on 14.03.2018.
 */

public interface TableDataSource {

    Flowable<List<Student>> loadStudents(boolean forceRemote,String token,String groupId);
    Flowable<ResList1> loadStudentsRemote(boolean forceRemote, String token, String groupId);
    Flowable<List<StudentGroup>> loadStudentGroups(boolean forceRemote);
    Flowable<List<Grade>> loadGrade(boolean forceRemote,String token,String courseId,String groupId);
    Flowable<ResList3> loadGradeRemote(boolean forceRemote, String token, String courseId, String groupId);
    Flowable<List<Practice>> loadPractice(boolean forceRemote,String token,String courseId,String groupId);
    Flowable<ResList2> loadPracticeRemote(boolean forceRemote, String token, String courseId, String groupId);

    Flowable<List<Practice>> loadUser(boolean forceRemote,String userId);
    Flowable<ResList2> loadUserRemote(boolean forceRemote, String token);


    Flowable<List<Course>> loadCourse(boolean forceRemote);
    Flowable<Grade> getGradeById(int studentId,long practiceId);

    void addGroup(StudentGroup studentGroup);

    Completable addStudent(Student student);
    Completable addGrade(Grade grade);
    Completable updateGrade(Grade grade);
    Completable addPractice(Practice practice);
    void addCourse(Course course);



    void clearData();

    Flowable<Res> logout(String token);

    Flowable<ResList> loadPredmet(boolean onlineReq, String token);

    Single<User> login(User user);

    Flowable<Res> loginOnline(User user);

    Flowable<Long> saveUser(User user);

    Single<List<Predmet>> loadPredmetOFline(boolean onlineRequired, String token, Long userId);

    Completable addPredmet(List<Predmet> predmets);

    Completable addStudentGroup(ArrayList<StudentGroup> studentGroupREsp);

    Single<List<StudentGroup>> loadStudentGroupsOfline(boolean onlineRequired, String token, Long userId);

    Completable saveStudet(List<Student> listStudent);

    Completable savePractice(List<Practice> listPractice);

    Completable saveGrade(List<Grade> listGrade);
}
