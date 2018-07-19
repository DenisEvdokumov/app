package com.example.teachergradebook.data.repository.local;

import com.example.teachergradebook.data.database.StudentGroupDao;
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
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Денис on 14.03.2018.
 */

public class LocalTableDataSource implements TableDataSource {

    private StudentGroupDao studentGroupDao;




    @Inject
    public LocalTableDataSource(StudentGroupDao studentGroupDao){
        this.studentGroupDao = studentGroupDao;
    }

    @Override
    public Flowable<List<Student>> loadStudents(boolean forceRemote,String token,String groupId) {
//        Student student = new Student("12");
//        List<Student> list = new ArrayList<Student>();
//        list.add(student);
        return studentGroupDao.getAllStudent();
    }

    @Override
    public Flowable<ResList1> loadStudentsRemote(boolean forceRemote, String token, String groupId) {
        return null;
    }

    @Override
    public Flowable<List<StudentGroup>> loadStudentGroups(boolean forceRemote) {
        return null;
    }

    @Override
    public Flowable<List<Grade>> loadGrade(boolean forceRemote,String token,String courseId,String groupId) {
        return studentGroupDao.getAllGrade(courseId,groupId);
    }

    @Override
    public Flowable<ResList3> loadGradeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return null;
    }

    @Override
    public Flowable<List<Practice>> loadPractice(boolean forceRemote,String token,String courseId,String groupId) {
         return studentGroupDao.getAllPractice();
    }

    @Override
    public Flowable<ResList2> loadPracticeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return null;
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
        return studentGroupDao.getAllCourse();
    }

    @Override
    public Flowable<Grade> getGradeById(int studentId, long practiceId) {
        return studentGroupDao.getGradeById(studentId,practiceId);
    }


    @Override
    public void addGroup(StudentGroup studentGroup) {
        studentGroupDao.insert(studentGroup);
    }

    @Override
    public Completable addStudent(Student student) {
        //Log.i("1","addStudentLocalDAta "+student.getName());

        return Completable.fromAction(() ->{
            studentGroupDao.insert(student);
        });

    }

    @Override
    public Completable addGrade(Grade grade) {
        return Completable.fromAction(() -> {
            studentGroupDao.insert(grade);
        });
    }

    @Override
    public Completable updateGrade(Grade grade) {
        return Completable.fromAction(() -> {
            studentGroupDao.update(grade);
        });
    }

    @Override
    public Completable addPractice(Practice practice) {
        return Completable.fromAction(() ->{
            studentGroupDao.insert(practice);
        });
    }

    @Override
    public void addCourse(Course course) {

    }

    @Override
    public void clearData() {
        studentGroupDao.deleteAllStudentGroup();
        studentGroupDao.deleteAllStudent();
    }

    @Override
    public Flowable<Res> logout(String token) {
        return null;
    }

    @Override
    public Flowable<ResList> loadPredmet(boolean onlineRequired, String token) {

        return null;
    }

    @Override
    public Single<User> login(User user) {
        return studentGroupDao.login(user.getLogin(),user.getPassword());
    }

    @Override
    public Flowable<Res> loginOnline(User user) {
        return null;
    }

    @Override
    public Flowable<Long> saveUser(User user) {

        return Flowable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return studentGroupDao.saveUser(user);
            }


            });
    }

    @Override
    public Single<List<Predmet>> loadPredmetOFline(boolean onlineRequired, String token, Long userId) {
        return studentGroupDao.getAllPredmet(userId);
    }

    @Override
    public Completable addPredmet(List<Predmet> predmets) {
        return Completable.fromAction(() ->{
            studentGroupDao.addPredmet(predmets);
        });

    }

    @Override
    public Completable addStudentGroup(ArrayList<StudentGroup> studentGroupREsp) {
        return Completable.fromAction(() -> {
            studentGroupDao.addStudentGroup(studentGroupREsp);
        });
    }

    @Override
    public Single<List<StudentGroup>> loadStudentGroupsOfline(boolean onlineRequired, String token, Long userId) {
        return studentGroupDao.getAllStudentGroup();
    }

    @Override
    public Completable saveStudet(List<Student> listStudent) {
        return Completable.fromAction(() -> {
            studentGroupDao.saveStudent(listStudent);
        });
    }

    @Override
    public Completable savePractice(List<Practice> listPractice) {
        return Completable.fromAction(() -> {
            studentGroupDao.savePractice(listPractice);
        });
    }

    @Override
    public Completable saveGrade(List<Grade> listGrade) {
        return Completable.fromAction(() -> {
            studentGroupDao.saveGrade(listGrade);
        });
    }


}
