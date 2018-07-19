package com.example.teachergradebook.UI.Table;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;


import com.example.teachergradebook.UI.Base.BasePresenter;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.ResList1;
import com.example.teachergradebook.data.model.ResList2;
import com.example.teachergradebook.data.model.ResList3;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.repository.TableDataSource;
import com.example.teachergradebook.data.repository.TableRepository;
import com.example.teachergradebook.util.schedulers.RunOn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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
        implements TableContract.Presenter , LifecycleObserver {

    private TableRepository repository;
    String groupId = "";
    String predmetId = "";
    String token = "";
    private TableContract.View view;
    Boolean onlineRequired;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private CompositeDisposable disposable;

    private Bundle viewStateBundle = getStateBundle();

    @Inject
    public TablePresenter(TableRepository repository, TableContract.View view,
                          @RunOn(IO) Scheduler ioScheduler, @RunOn(UI) Scheduler uiScheduler) {
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
    public void onDetachLifeCycle() {
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
        if (onlineRequired) {
          //  if (this.onlineRequired){

                disposable.add(
                        Flowable.combineLatest(
                                repository.loadStudentsRemote(onlineRequired, token, groupId)
                                        .subscribeOn(ioScheduler)
                                        .observeOn(uiScheduler),
                                repository.loadPracticeRemote(onlineRequired, token, predmetId, groupId)
                                        .subscribeOn(ioScheduler)
                                        .observeOn(uiScheduler),

                                (a, b) ->
                                        CombineRemote(a, b))

                                .subscribe(this::handleReturnedPractice));
//                );


           // this.onlineRequired = false;
//        }
        } else {
            view.clearQuestions();

            //Load new one and populate it into view
            disposable.add(
                    Flowable.combineLatest(
                            repository.loadStudents(onlineRequired, token, groupId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),
                            repository.loadPractice(onlineRequired, token, groupId, predmetId)
                                    .subscribeOn(ioScheduler)
                                    .observeOn(uiScheduler),

                            (a, b) -> Combine(a, b))

                            .subscribe(this::handleReturnedPractice)
            );
        }
    }




    private ArrayList CombineRemote(ResList1 resListStudetn, ResList2 resListsPractice) {
        ArrayList arrayList = new ArrayList();

        ArrayList<Student> students = (ArrayList<Student>) resListStudetn.getData();
        ArrayList<Practice> practices = (ArrayList<Practice>) resListsPractice.getData();
        ArrayList<Grade> grades = new ArrayList<>();
        arrayList.add(students);
        arrayList.add(practices);
        grades = loadGrade(token,groupId,predmetId);
//        TODO When api grade.php
//        arrayList.add(resListsGrade.getData());
        List<Grade> gradesNON = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < practices.size(); j++) {

                Grade grade = new Grade();
                grade.setPracticeId(practices.get(j).getId());
                grade.setStudentId(students.get(i).getId());
                grade.setName(" ");
                gradesNON.add(k, grade);
                k++;


            }
        }
        for (Grade grade: gradesNON){
            for(Grade grade1:grades){
                if (grade.getPracticeId()==grade1.getPracticeId()&
                        grade.getStudentId()==grade1.getStudentId()){
                    grade.setName(grade1.getName());
                }
            }
        }
        arrayList.add(gradesNON);

        return arrayList;
    }

    private ArrayList Combine(List<Student> a, List<Practice> b) {

        ArrayList arrayList = new ArrayList();
        ArrayList<Grade> grades = new ArrayList<>();
        arrayList.add(a);
        arrayList.add(b);
        //arrayList.add(c);
        for (Student student:a){


        disposable.add(
        repository.loadGrade(onlineRequired, token, predmetId, String.valueOf(student.getId()))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)

                .subscribe((q) -> {
                    for (Grade grade:q){
                    grades.add(grade);}})
            );
        }
        arrayList.add(grades);
        return arrayList;
    }


    private void handleReturnedPractice(ArrayList arrayList) {
        view.stopLoadingIndicator();
        List<Student> listStudent = (List<Student>) arrayList.get(0);
        List<Practice> listPractice = (List<Practice>) arrayList.get(1);
        List<Grade> listGrade = (List<Grade>) arrayList.get(2);
        List<List<Grade>> sortlistGrade = SortGrade(listGrade, listStudent, listPractice);
//        for (Grade grade:listGrade){
//           // updateGrade(grade);
//        }
//        for (Student student:listStudent){
//            addStudent(student.getName(),student.getId());
//        }
//
//        for (Practice practice:listPractice){
//            addPractice(Long.valueOf(groupId),practice.getId());
//        }



        if (listStudent != null && !listStudent.isEmpty() &&
                listPractice != null && !listPractice.isEmpty() &&
                listGrade != null && !listGrade.isEmpty()) {
            view.showTable(listStudent, listPractice, sortlistGrade);
        } else {
            view.showNoDataMessage();
        }
        if(onlineRequired) {
            disposable.add(
                    repository.saveStudet(listStudent)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe(() -> Log.i("1", "Saved Student")));
            disposable.add(
                    repository.savePractice(listPractice)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe(() -> Log.i("1", "Saved Student")));
            disposable.add(
                    repository.saveGrade(listGrade)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe(() -> Log.i("1", "Saved Student")));
            onlineRequired=false;
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

//
//        disposable.add(
//                repository.loadPractice(false,"","","")
//                        .subscribeOn(ioScheduler)
//                        .observeOn(uiScheduler)
//                .subscribe(a -> addGradeS(a,student.getId()))
//        );

        refreshTableAfterPaused();

    }



    private void addGradeS(List<Practice> practices, int studentId) {
        for (Practice practice:practices){
            Log.i("1","OLOLOLOLOLOLOL"+studentId);
            Grade grade = new Grade();
            grade.setPracticeId(practice.getId());
            grade.setStudentId(studentId);


            grade.setName("p" + practice.getId() + "s" + studentId);
           // addGrade(grade);

        }
    }
    private void addGradeP(List<Student> students, long practiceId) {
        for (Student student: students){
            Grade grade = new Grade();
            grade.setPracticeId(practiceId);
            grade.setStudentId(student.getId());


            grade.setName("p"  + practiceId + "s" + student.getId());
          //  addGrade(grade);
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
//        disposable.add(
//                repository.loadStudents(false,"","")
//                        .subscribeOn(ioScheduler)
//                        .observeOn(uiScheduler)
//                        .subscribe(a -> addGradeP(a,practice.getId()))
//        );


        refreshTableAfterPaused();

    }

    @Override
    public void logout(String token) {
        disposable.add(
                repository.logout(token)
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)

                        .subscribe((a)-> Log.i("1","logout")));
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
                            //loadTable(false);

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }

//    private List<Grade> loadGrade() {


        private ArrayList<Grade> loadGrade(String token, String groupId, String courseId) {
            ArrayList<Grade> listGrage = new ArrayList<>();
        if(true) {
            String[] resp;

            ArrayList<StudentGroup> studentGroupREsp = new ArrayList<>();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            URL url;
            StringBuffer response = new StringBuffer();
            try {
                url = new URL("http://gw.comsys.kpi.ua:10080/mygb/api/teacher/listmarks.php/?token=" + token + "&courseid=" + courseId + "&groupid=" + groupId);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("invalid url");
            }

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(false);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                // handle the response
                int status = conn.getResponseCode();
                if (status != 200) {
                    throw new IOException("Post failed with error code " + status);
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                Log.i("1", "token " + token);
                Log.i("1", "courseId " + courseId);
                Log.i("1", "groupId " + groupId);
                //Here is your json in string format
                Log.i("1", response.toString());
                if (response.toString().length() > 20) {
                    resp = response.toString().split("\\}\\},\"");
                    int k = 0;


                    String practiceId;
                    String studentId = "0";

                    for (int i = 0; i < resp.length; i++) {
                        String[] resp1 = resp[i].split("\"\\},\"");

                        for (int j = 0; j < resp1.length; j++) {
//                        Log.i("1",resp1[j]);
//                        Log.i("1","------------------");
                            Grade grade = new Grade();
                            if (i == 0 & j == 0) {
                                String[] resp2 = resp1[j].split("\":\\{\"");
                                studentId = resp2[1];
                                grade.setStudentId(Long.valueOf(resp2[1]));
                                Log.i("1", "studentId " + resp2[1]);
                                grade.setPracticeId(Long.valueOf(resp2[2]));
                                Log.i("1", "practiceId " + resp2[2]);
                                String[] resp3 = resp2[3].split("\"");
                                grade.setName(resp3[2]);
                                Log.i("1", "Name " + resp3[2]);

                            } else {
                                if (j == 0) {
                                    String[] resp2 = resp1[j].split("\"");
                                    studentId = resp2[0];
                                    grade.setStudentId(Long.valueOf(resp2[0]));
                                    grade.setPracticeId(Long.valueOf(resp2[2]));
                                    grade.setName(resp2[6]);

                                    Log.i("1", "studentId " + resp2[0]);

                                    Log.i("1", "practiceId " + resp2[2]);
                                    Log.i("1", "Name" + resp2[6]);


                                } else {
                                    String[] resp2 = resp1[j].split("\"");
                                    grade.setStudentId(Long.valueOf(studentId));
                                    grade.setPracticeId(Long.valueOf(resp2[0]));
                                    grade.setName(resp2[4]);

                                    Log.i("1", "studentId " + studentId);

                                    Log.i("1", "practiceId " + resp2[0]);
                                    Log.i("1", "Name" + resp2[4]);
                                }

                            }


//

                            listGrage.add(grade);
//
                        }
                        Log.i("1", "------------");
                        //Log.i("1",resp[i]);
//                    StudentGroup studentGroup = new StudentGroup();
//                    String [] resp1 = resp[i].split(",");
//                    String [] resp2 = resp[i+1].split(",");
//                    String groupId = String.copyValueOf(resp1[0].toCharArray(),3,resp1[0].toCharArray().length-4);
//                    String groupName = String.copyValueOf(resp1[1].toCharArray(),8,resp1[1].toCharArray().length-9);
//                    String groupPracticeId = String.copyValueOf(resp2[0].toCharArray(),3,resp2[0].toCharArray().length-4);
//                Log.i("1","groupId " + groupId);
//                Log.i("1","groupName " + groupName);
//                Log.i("1","groupPracticeId " + groupPracticeId);
//                    studentGroup.setId(Long.valueOf(groupId));
//                    studentGroup.setPredmetId(Long.valueOf(groupPracticeId));
//                    studentGroup.setName(groupName);
//                    studentGroupREsp.add(studentGroup);

//                    for(String s:resp1) {
//                        Log.i("1", s);
//                    }
//                Log.i("1", "------------------------------------------");
//                for(String s:resp2) {
//                    Log.i("1", s);
//                }
                        //i++;
                        //Log.i("1", "---------------------------------------------------------------------------------------------------------");

//               char[] chars =resp[i].toCharArray();
//                StudentGroup studentGroup = new StudentGroup();
//                String s = String.copyValueOf(chars,3,6);
//
//                studentGroup.setId(Long.valueOf());

                    }
                }
            }
        }
            return listGrage;

    }


}

