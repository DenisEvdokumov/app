package com.example.teachergradebook.UI.Table;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.example.teachergradebook.UI.Base.BasePresenter;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.ResList;
import com.example.teachergradebook.data.model.ResList1;
import com.example.teachergradebook.data.model.ResList2;
import com.example.teachergradebook.data.model.ResList3;
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
     String groupId="";
     String courceId="";
     String token="";
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
        //loadTable(true);

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
        if(onlineRequired){
            disposable.add(
                    Flowable.combineLatest(
                            repository.loadStudentsRemote(onlineRequired,token,groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            repository.loadPracticeRemote(onlineRequired,token,courceId,groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            repository.loadGradeRemote(onlineRequired,token,courceId,groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            (a, b, c) -> CombineRemote(a, b, c))

                            .subscribe(this::handleReturnedPractice)
            );
        }else {
            view.clearQuestions();

            //Load new one and populate it into view
            disposable.add(
                    Flowable.combineLatest(
                            repository.loadStudents(onlineRequired,token,groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            repository.loadPractice(onlineRequired,token,groupId,courceId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            repository.loadGrade(onlineRequired,token,courceId,groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            (a, b, c) -> Combine(a, b, c))

                            .subscribe(this::handleReturnedPractice)
            );
        }
    }



    private ArrayList CombineRemote(ResList1 resListStudetn, ResList2 resListsPractice, ResList3 resListsGrade) {
        ArrayList arrayList = new ArrayList();

        ArrayList<Student> students = (ArrayList<Student>) resListStudetn.getData();
        ArrayList<Practice> practices = (ArrayList<Practice>) resListsPractice.getData();
        arrayList.add(students);
        arrayList.add(practices);
//        TODO When api grade.php
//        arrayList.add(resListsGrade.getData());
        List<Grade> grades = new ArrayList<>();
        int k=0;
        for (int i = 0; i < students.size();i++) {
        for (int j = 0 ;j < practices.size();j++){

                Grade grade = new Grade(practices.get(j).getId(),students.get(i).getId());
                grade.setName(" ");
                grades.add(k,grade);
                k++;


            }}
        arrayList.add(grades);
        return arrayList;
    }

    private  ArrayList Combine(List<Student> a, List<Practice> b, List<Grade> c) {

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
        List<Grade> listGrade = (List<Grade>) arrayList.get(2);
        List<List<Grade>> sortlistGrade = SortGrade(listGrade,listStudent,listPractice);


        if (listStudent !=null && !listStudent.isEmpty() &&
                listPractice !=null && !listPractice.isEmpty() &&
                listGrade !=null && !listGrade.isEmpty() ){
            view.showTable(listStudent,listPractice,sortlistGrade);
        } else {
            view.showNoDataMessage();
        }
    }


    private List<List<Grade>> SortGrade(List<Grade> listGrade,
                                        List<Student> listStudent, List<Practice> listPractice) {
        Log.i("1",listGrade.size() + "size");
        List<List<Grade>> newlistGrade = new ArrayList<>();

        for (int i = 0;i<listStudent.size();i++){
            List<Grade> newlistGrade1 = new ArrayList<>();
            for (int j=0; j< listPractice.size();j++){
                for(int k = 0;k<listGrade.size();k++){
                    if(listPractice.get(j).getId()==listGrade.get(k).getPracticeId() &
                            listStudent.get(i).getId()==listGrade.get(k).getStudentId()){
                        newlistGrade1.add(listGrade.get(k));
                    }
                }
//                disposable.add(repository.getGradeById(listStudent.get(i).getId(),listPractice.get(j).getId())
//                        .subscribeOn(ioScheduler)
//                        .observeOn(uiScheduler)
//                        .subscribe(a    -> Log.i("1",listStudent.get(0).getId() +" "+ listPractice.get(0).getId()+" "+ a.getName()),
//                                throwable -> Log.i("1", "Unable to update username" + throwable)));


            }
            newlistGrade.add(newlistGrade1);

        }
        return newlistGrade;
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
    public void updateGrade(Grade grade) {

        disposable.add(repository.updateGrade(grade)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> refreshTableAfterPaused(),
                        throwable -> Log.i("1", "Unable to update username" + throwable)));


    }

    @Override
    public void addStudent(String name,long groupId) {

        Student student = new Student(name,groupId);
        student.setId((int) (Math.random()*10000));
        student.setName(String.valueOf(student.getId()));
        disposable.add(repository.addStudent(student)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> Log.i("1","addStudent TAblePresenter"),
                        throwable -> Log.i("1", "Unable to update username" + throwable)));


        disposable.add(
                repository.loadPractice(false,"","","")
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)
                .subscribe(a -> addGradeS(a,student.getId()))
        );

        refreshTableAfterPaused();

    }



    private void addGradeS(List<Practice> practices, int studentId) {
        for (Practice practice:practices){
            Log.i("1","OLOLOLOLOLOLOL"+studentId);
            Grade grade = new Grade(practice.getId(),studentId);
            grade.setName("p" + practice.getId() + "s" + studentId);
            addGrade(grade);

        }
    }
    private void addGradeP(List<Student> students, long practiceId) {
        for (Student student: students){
            Grade grade = new Grade(practiceId,student.getId());
            grade.setName("p"  + practiceId + "s" + student.getId());
            addGrade(grade);
        }
    }


    private void addGrade(Grade grade) {
        disposable.add(repository.addGrade(grade)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> Log.i("1","addGrade"),
                        throwable -> Log.i("1", "Unable to update grage" + throwable)));


    }


    @Override
    public void addPractice(long studentGroupId,long predmetId) {
        Practice practice = new Practice(studentGroupId,predmetId);

        practice.setId((long) (Math.random()*10000));
        practice.setName(String.valueOf(practice.getId()));
        disposable.add(repository.addPractice(practice)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(()-> Log.i("1","addPractice"),
                        throwable -> Log.i("1", "Unable to update practice" + throwable)));
        disposable.add(
                repository.loadStudents(false,"","")
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)
                        .subscribe(a -> addGradeP(a,practice.getId()))
        );


        refreshTableAfterPaused();

    }

    @Override
    public void logout(String token) {
        disposable.add(
                repository.logout(token)
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)

                        .subscribe((a)-> view.showErrorMessage("s"),
                                throwable -> view.showErrorMessage("s")));
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




    private void refreshTableAfterPaused() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);

                    new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                        @Override
                        public void run() {
//                            animatedCircleLoadingView.setEnabled(true);
                            loadTable(false);

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }




}

