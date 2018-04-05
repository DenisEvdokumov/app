package com.example.teachergradebook.data.repository;

import android.util.Log;

import com.example.teachergradebook.data.model.Course;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.repository.local.Local;
import com.example.teachergradebook.data.repository.local.LocalTableDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Денис on 14.03.2018.
 */

public class TableRepository implements TableDataSource {

    private LocalTableDataSource localTableDataSource;

   // @VisibleForTesting List<Student> caches;

    @Inject public TableRepository(@Local LocalTableDataSource localTableDataSource){
        this.localTableDataSource = localTableDataSource;
        //caches = new ArrayList<>();
    }

    @Override
    public Flowable<List<Student>> loadStudents(boolean forceRemote) {
        return localTableDataSource.loadStudents(forceRemote)
                .take(1);
//                .flatMap(Flowable::fromIterable)
////                .doOnNext(student -> caches.add(student) )
//                .toList()
//                .toFlowable()
//                .filter(list -> !list.isEmpty());
                //.switchIfEmpty(

                        //refreshData()); // If local data is empty, fetch from remote source instead.

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
    public Flowable<List<Grade>> loadGrade(boolean forceRemote) {
        return null;
    }

    @Override
    public Flowable<List<Practice>> loadPractice(boolean forceRemote) {

        return localTableDataSource.loadPractice(forceRemote)
                .take(1);
    }

    @Override
    public Flowable<List<Course>> loadCourse(boolean forceRemote) {
        return null;
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
                    .subscribe(()-> Log.i("1","addStudetTableRepository "+student.getName()),
                            throwable -> Log.i("1", "Unable to update username" + throwable));
            ;
        });

    }

    @Override
    public void addGrade(Grade grade) {

    }

    @Override
    public Completable addPractice(Practice practice) {
        return Completable.fromAction(() ->{
            localTableDataSource.addPractice(practice)
                    .subscribe(()-> Log.i("1","addStudetTableRepository "+practice.getName()),
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
}
