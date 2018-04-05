package com.example.teachergradebook.UI.Table;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.util.Log;


import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.UI.Base.BasePresenter;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.repository.TableRepository;
import com.example.teachergradebook.util.schedulers.RunOn;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;


import static com.example.teachergradebook.util.schedulers.SchedulerType.UI;
import static com.example.teachergradebook.util.schedulers.SchedulerType.IO;

/**
 * Created by Денис on 19.03.2018.
 */

public class TablePresenter extends BasePresenter<TableContract.View>
        implements TableContract.Presenter , LifecycleObserver  {

    private TableRepository repository;

    private TableContract.View view;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private CompositeDisposable disposable;
    
    private Bundle viewStateBundle = getStateBundle();

    @Inject public TablePresenter(TableRepository repository, TableContract.View view,
                                  @RunOn(IO)Scheduler ioScheduler, @RunOn(UI) Scheduler uiScheduler){
        this.repository = repository;
        this.view = view;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;

        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);

        }

        disposable = new CompositeDisposable();
    }


    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttachView() {
        //loadTable(false);

    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDetachLifeCycle(){
        ((LifecycleOwner) view).getLifecycle().removeObserver(this);
        view = null;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetachView() {
        disposable.clear();

    }

    @Override
    public void loadTable(boolean onlineRequired) {
        //Clear old data on view
        view.clearQuestions();

        //Load new one and populate it into view
        disposable.add(
         Flowable.combineLatest(
                        repository.loadStudents(onlineRequired)
                                .subscribeOn(ioScheduler)
                                .observeOn(uiScheduler),
                        repository.loadPractice(onlineRequired)
                                .subscribeOn(ioScheduler)
                                .observeOn(uiScheduler),
                        repository.loadPractice(onlineRequired)
                                .subscribeOn(ioScheduler)
                                .observeOn(uiScheduler),
                        (a, b, c) -> Combine(a, b,c))

                 .subscribe(this::handleReturnedPractice)
        );

    }
    private  ArrayList Combine(List<Student> a, List<Practice> b, List<Practice> c) {
        for (Student student:a){
            Log.i("1",""+student.getName());
        }

        Log.i("1",""+b.size());
        ArrayList arrayList = new ArrayList();
        arrayList.add(a);
        arrayList.add(b);
        arrayList.add(c);
        return arrayList;
    }


    private void handleReturnedPractice(ArrayList arrayList) {
        view.stopLoadingIndicator();
        List<Student> listStudent = (List<Student>) arrayList.get(0);
        List<Practice> listPractice = (List<Practice>) arrayList.get(1);
        List<Practice> listGrade = (List<Practice>) arrayList.get(2);


        if (listStudent !=null && !listStudent.isEmpty() &&
                listPractice !=null && !listPractice.isEmpty() &&
                listGrade !=null && !listGrade.isEmpty() ){
            view.showTable(listStudent,listPractice,listGrade);
        } else {
            view.showNoDataMessage();
        }
    }


    @Override
    public void deleteGroup(Group group) {

    }

    @Override
    public void addGrade(String grade) {
//        Grade.setName(grade);
//        repository.addGrade(Grade);

    }

    @Override
    public void addStudent() {
        Student student = new Student();

        student.setName("Peter"+ Math.random());
        disposable.add(repository.addStudent(student)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> Log.i("1","addStudent"),
                        throwable -> Log.i("1", "Unable to update username" + throwable)));

    }

    @Override
    public void addPractice() {
        Practice practice = new Practice();
        practice.setName("Lab"+Math.random());
        disposable.add(repository.addPractice(practice)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> Log.i("1","addPractice"),
                        throwable -> Log.i("1", "Unable to update username" + throwable)));


    }

    private void handleError(Throwable throwable) {
        Log.i("1","TablePresenter HandleError"+throwable.toString());
        view.stopLoadingIndicator();
        view.showErrorMessage(throwable.getLocalizedMessage());
    }

    private void handleReturnedStudent(List<Student> listStudent,List<Practice> listPractice) {
        view.stopLoadingIndicator();
        if (listStudent !=null && !listStudent.isEmpty()){
            view.showStudents(listStudent);
        } else {
            view.showNoDataMessage();
        }
    }








}

