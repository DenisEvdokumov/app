package com.example.teachergradebook.data.repository;

import android.util.Log;

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
import com.example.teachergradebook.data.repository.local.Local;
import com.example.teachergradebook.data.repository.local.LocalTableDataSource;
import com.example.teachergradebook.data.repository.remote.Remote;
import com.example.teachergradebook.data.repository.remote.RemoteTableDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Денис on 14.03.2018.
 */

public class TableRepository implements TableDataSource {

    private LocalTableDataSource localTableDataSource;
    private RemoteTableDataSource remoteTableDataSource;

   // @VisibleForTesting List<Student> caches;

    @Inject public TableRepository(@Local LocalTableDataSource localTableDataSource,
                                   @Remote RemoteTableDataSource remoteDataSource){
        this.localTableDataSource = localTableDataSource;
        this.remoteTableDataSource = remoteDataSource;
        //caches = new ArrayList<>();
    }



    @Override
    public Flowable<List<Student>> loadStudents(boolean forceRemote,String token,String groupId) {

            return localTableDataSource.loadStudents(forceRemote,token,groupId)
                    .take(1);

//        return localTableDataSource.loadStudents(forceRemote)
//                .take(1);
//                .flatMap(Flowable::fromIterable)
////                .doOnNext(student -> caches.add(student) )
//                .toList()
//                .toFlowable()
//                .filter(list -> !list.isEmpty());
                //.switchIfEmpty(

                        //refreshData()); // If local data is empty, fetch from remote source instead.

    }

    @Override
    public Flowable<ResList1> loadStudentsRemote(boolean forceRemote, String token, String groupId) {
        return remoteTableDataSource.loadStudentsRemote(forceRemote,token,groupId)
                .take(1);
    }

    /**
     * Fetches data from remote source.
     * Save it into both local database and cache.
     *
     * @return the Flowable of newly fetched data.
     */
    Flowable<List<Student>> refreshData() {
        return null;
    }
    Flowable<List<StudentGroup>> refreshStudentGroup() {
        return null;
    }


    @Override
    public Flowable<List<StudentGroup>> loadStudentGroups(boolean forceRemote) {
        return localTableDataSource.loadStudentGroups(false)
                .take(1)
                .flatMap(Flowable::fromIterable)
                .toList()
                .toFlowable()
                .filter(list -> list.isEmpty())
                .switchIfEmpty(refreshStudentGroup());
    }

    @Override
    public Flowable<List<Grade>> loadGrade(boolean forceRemote,String token,String courseId,String groupId) {

            return localTableDataSource.loadGrade(forceRemote,token,courseId,groupId)
                    .take(1);

//        return localTableDataSource.loadGrade(forceRemote,token,courseId,groupId)
//                .take(1);
    }

    @Override
    public Flowable<ResList3> loadGradeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return remoteTableDataSource.loadGradeRemote(forceRemote,token,courseId,groupId);
    }

    @Override
    public Flowable<List<Practice>> loadPractice(boolean forceRemote,String token,String courseId,String groupId) {


            return localTableDataSource.loadPractice(forceRemote,token,courseId,groupId)
                    .take(1);
      }

    @Override
    public Flowable<ResList2> loadPracticeRemote(boolean forceRemote, String token, String courseId, String groupId) {
        return remoteTableDataSource.loadPracticeRemote(forceRemote,token,courseId,groupId)
                .take(1);
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

        return localTableDataSource.getGradeById(studentId,practiceId);
    }

    @Override
    public void addGroup(StudentGroup studentGroup) {
        localTableDataSource.addGroup(studentGroup);

    }

    @Override
    public Completable addStudent(Student student) {


        return Completable.fromAction(() ->{
            //Log.i("1","addStudetTableRepository"+student.getName());
            localTableDataSource.addStudent(student)
                    .subscribe(()-> Log.i("1","addStudetTableRepository "),
                            throwable -> Log.i("1", "Unable to update username" + throwable));
            ;
        });

    }

    @Override
    public Completable addGrade(Grade grade) {
        return Completable.fromAction(() ->{
            localTableDataSource.addGrade(grade)
                    .subscribe(()-> Log.i("1","addGradeTableRepository "),
                            throwable -> Log.i("1", "Unable to update grade" + throwable));
        });

    }

    @Override
    public Completable updateGrade(Grade grade) {
        return Completable.fromAction(() ->{
            localTableDataSource.updateGrade(grade)
                    .subscribe(()-> Log.i("1","addGradeTableRepository "),
                            throwable -> Log.i("1", "Unable to update username" + throwable));


        });
    }

    @Override
    public Completable addPractice(Practice practice) {
        return Completable.fromAction(() ->{
            localTableDataSource.addPractice(practice)
                    .subscribe(()-> Log.i("1","addGradeTableRepository "),
                            throwable -> Log.i("1", "Unable to update username" + throwable));


        });
    }

    @Override
    public void addCourse(Course course) {

    }

    @Override
    public void clearData() {
        //caches.clear();
        localTableDataSource.clearData();

    }

    @Override
    public Flowable<Res> logout(String token) {
        return  remoteTableDataSource.logout(token);
    }

    @Override
    public Flowable<ResList> loadPredmet(boolean onlineRequired, String token) {

        return remoteTableDataSource.loadPredmet(onlineRequired,token);

    }

    @Override
    public Single<User> login(User user) {
        return localTableDataSource.login(user);
    }

    @Override
    public Flowable<Res> loginOnline(User user) {
        return remoteTableDataSource.loginOnline(user);
    }

    @Override
    public Flowable<Long> saveUser(User user) {
        return
            localTableDataSource.saveUser(user);
    }

    @Override
    public Single<List<Predmet>> loadPredmetOFline(boolean onlineRequired, String token, Long userId) {
        return localTableDataSource.loadPredmetOFline(onlineRequired,token,userId);
    }

    @Override
    public Completable addPredmet(List<Predmet> predmets) {
        return localTableDataSource.addPredmet(predmets);
    }


}
