package com.example.teachergradebook.data.repository.remote;


import com.example.teachergradebook.UI.Table.TablePresenter;
import com.example.teachergradebook.data.api.ServerApi;
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
import com.example.teachergradebook.data.repository.TableDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class RemoteTableDataSource implements TableDataSource {

    ServerApi serverApi;

    @Inject
    public RemoteTableDataSource(ServerApi serverApi){
        this.serverApi = serverApi;
    }

    public Flowable<Res> loginOnline(User user){
        return serverApi.login(user.getLogin(),user.getPassword());
    }

    @Override
    public Flowable<Long> saveUser(User user) {
        return null;
    }

    @Override
    public Single<List<Predmet>> loadPredmetOFline(boolean onlineRequired, String token, Long userId) {
        return null;
    }

    @Override
    public Completable addPredmet(List<Predmet> predmets) {
        return null;
    }

    @Override
    public Completable addStudentGroup(ArrayList<StudentGroup> studentGroupREsp) {
        return null;
    }

    @Override
    public Single<List<StudentGroup>> loadStudentGroupsOfline(boolean onlineRequired, String token, Long userId) {
        return null;
    }

    @Override
    public Completable saveStudet(List<Student> listStudent) {
        return null;
    }

    @Override
    public Completable savePractice(List<Practice> listPractice) {
        return null;
    }

    @Override
    public Completable saveGrade(List<Grade> listGrade) {
        return null;
    }

    @Override
    public Single<User> login(User user) {
        return null;
    }


    @Override
    public Flowable<List<Student>> loadStudents(boolean forceRemote,String token,String groupID) {

        return null;
    }

    @Override
    public Flowable<ResList1> loadStudentsRemote(boolean forceRemote, String token, String groupId) {
        return serverApi.loadStudent(token,groupId);
    }

    @Override
    public Flowable<List<StudentGroup>> loadStudentGroups(boolean forceRemote) {
        return null;
    }

    @Override
    public Flowable<List<Grade>> loadGrade(boolean forceRemote,String token,String courseId,String groupId) {
        return null;
    }

    @Override
    public Flowable<ResList3> loadGradeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return serverApi.loadGrade(token,courseId,"20");
    }


    @Override
    public Flowable<List<Practice>> loadPractice(boolean forceRemote,String token,String courseId,String groupId) {
        return null;
    }

    @Override
    public Flowable<ResList2> loadPracticeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return serverApi.loadPractice(token,courseId,groupId);
    }

    @Override
    public Flowable<List<Practice>> loadUser(boolean forceRemote, String userId) {
        return null;
    }

    @Override
    public Flowable<ResList2> loadUserRemote(boolean forceRemote, String token) {
        return null;
    }


    @Override
    public Flowable<List<Course>> loadCourse(boolean forceRemote) {
        return null;
    }

    @Override
    public Flowable<Grade> getGradeById(int studentId, long practiceId) {
        return null;
    }

    @Override
    public void addGroup(StudentGroup studentGroup) {

    }

    @Override
    public Completable addStudent(Student student) {
        return null;
    }

    @Override
    public Completable addGrade(Grade grade) {
        return null;
    }

    @Override
    public Completable updateGrade(Grade grade) {
        return null;
    }

    @Override
    public Completable addPractice(Practice practice) {
        return null;
    }

    @Override
    public void addCourse(Course course) {

    }

    @Override
    public void clearData() {

    }

    @Override
    public Flowable<Res> logout(String token) {
        return serverApi.logout(token);
    }

    @Override
    public Flowable<ResList> loadPredmet(boolean onliteReq,String token) {

        return serverApi.loadPredmet(token);
    }
}
