package com.example.teachergradebook.UI.PractieGroupList;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.example.teachergradebook.UI.Base.BasePresenter;

import com.example.teachergradebook.data.model.Predmet;

import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;
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
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

import static com.example.teachergradebook.util.schedulers.SchedulerType.IO;
import static com.example.teachergradebook.util.schedulers.SchedulerType.UI;

/**
 * Created by Денис on 19.03.2018.
 */

public class PGPresenter extends BasePresenter<PGContract.View>
        implements PGContract.Presenter , LifecycleObserver  {

    private TableRepository repository;

    private PGContract.View view;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private CompositeDisposable disposable;
    
    private Bundle viewStateBundle = getStateBundle();

    @Inject public PGPresenter(TableRepository repository, PGContract.View view,
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
//        loadTable(false);

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
    public void loadTable(boolean onlineRequired,String token,Long userId) {
        view.clearQuestions();
        if(onlineRequired) {
            ArrayList<StudentGroup> studentGroups = loadJson(token);
            //Load new one and populate it into view
            disposable.add(
                    repository.loadPredmet(onlineRequired, token)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe((a) ->
                                    {


                                        for(Predmet predmet :a.getData()){
                                            predmet.setUserId(userId);
                                        }
                                        disposable.add(
                                                repository.addPredmet(a.getData())
                                                        .subscribeOn(ioScheduler)
                                                        .observeOn(uiScheduler)

                                                        .subscribe(() ->
                                                                {


                                                                },
                                                                throwable -> view.showErrorMessage(throwable.getMessage())));
                                        view.showTable(a.getData(),studentGroups);
                                    },
                                    throwable -> view.showErrorMessage(throwable.getMessage())));


        }else {

            disposable.add(
                    repository.loadPredmetOFline(onlineRequired, token, userId)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe((a) -> {
                                        repository.loadStudentGroupsOfline(onlineRequired, token, userId)
                                                .subscribeOn(ioScheduler)
                                                .observeOn(uiScheduler)

                                                .subscribe((b) -> view.showTable(a,b),
                                                        throwable -> view.showErrorMessage(throwable.getMessage()));

                                    },
                                    throwable -> view.showErrorMessage(throwable.getMessage())));

        }
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

    @Override
    public void login(String login, String password) {
        User user = new User();
        user.setPassword(password);
        user.setLogin(login);
        disposable.add(
                repository.loginOnline(user)
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)

                        .subscribe((a)->{
                                    user.setToken(a.getData().getToken());

                                    disposable.add(
                                            repository.saveUser(user)
                                                    .subscribeOn(ioScheduler)
                                                    .observeOn(uiScheduler)

                                                    .subscribe((b)-> loadTable(true,a.getData().getToken(),b),
                                                            throwable -> Log.i("1", "Unable to update user" + throwable)));


                                },
                                throwable -> view.showError(throwable.getMessage())));


    }

    @Override
    public void saveStudentGroup(ArrayList<StudentGroup> studentGroupREsp) {
        disposable.add(
                repository.addStudentGroup(studentGroupREsp)
                        .subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)

                        .subscribe(() ->
                                {


                                },
                                throwable -> view.showErrorMessage(throwable.getMessage())));

    }


    private ArrayList<StudentGroup> loadJson(String token) {
        String [] resp ;
        ArrayList<StudentGroup> studentGroupREsp = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        URL url;
        StringBuffer response = new StringBuffer();
        try {
            url = new URL("http://gw.comsys.kpi.ua:10080/mygb/api/teacher/listgroupscourses.php/?token="+token);
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

            //Here is your json in string format
            resp= response.toString().split("id");
            int k=0;
            for(int i =1;i<resp.length;i++){
                StudentGroup studentGroup = new StudentGroup();
                String [] resp1 = resp[i].split(",");
                String [] resp2 = resp[i+1].split(",");
                String groupId = String.copyValueOf(resp1[0].toCharArray(),3,resp1[0].toCharArray().length-4);
                String groupName = String.copyValueOf(resp1[1].toCharArray(),8,resp1[1].toCharArray().length-9);
                String groupPracticeId = String.copyValueOf(resp2[0].toCharArray(),3,resp2[0].toCharArray().length-4);
//                Log.i("1","groupId " + groupId);
//                Log.i("1","groupName " + groupName);
//                Log.i("1","groupPracticeId " + groupPracticeId);
                studentGroup.setId(Long.valueOf(groupId));
                studentGroup.setPredmetId(Long.valueOf(groupPracticeId));
                studentGroup.setName(groupName);
                studentGroupREsp.add(studentGroup);

//                    for(String s:resp1) {
//                        Log.i("1", s);
//                    }
//                Log.i("1", "------------------------------------------");
//                for(String s:resp2) {
//                    Log.i("1", s);
//                }
                i++;
                Log.i("1", "---------------------------------------------------------------------------------------------------------");

//               char[] chars =resp[i].toCharArray();
//                StudentGroup studentGroup = new StudentGroup();
//                String s = String.copyValueOf(chars,3,6);
//
//                studentGroup.setId(Long.valueOf());
            }
        }
        saveStudentGroup(studentGroupREsp);
        return studentGroupREsp;
    }

//    private ArrayList Combine(List<Predmet> a, List<StudentGroup> b) {
//
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(a);
//        arrayList.add(b);
//        return arrayList;
//    }
//
//    private void handleReturnedPractice(ArrayList arrayList) {
//        view.stopLoadingIndicator();
//        List<Predmet> predmetList = (List<Predmet>) arrayList.get(0);
//        List<StudentGroup> groupList = (List<StudentGroup>) arrayList.get(2);
//        List<List<StudentGroup>> sortlistGroup = SortGrade(predmetList,groupList);
//
//
//        if (predmetList !=null && !predmetList.isEmpty() &&
//                groupList !=null && !groupList.isEmpty() ){
//            view.showTable(predmetList,sortlistGroup);
//        } else {
//            view.showNoDataMessage();
//        }
//    }
//
//    private List<List<StudentGroup>> SortGrade(List<Predmet> predmetList,
//                                        List<StudentGroup> listStudentGroup) {
//        Log.i("1",predmetList.size() + "size");
//        List<List<StudentGroup>> newlistStudentGroup = new ArrayList<>();
//
//        for (int i = 0;i<predmetList.size();i++){
//            List<StudentGroup> newListStudentGroup1 = new ArrayList<>();
//
//                for(int k = 0;k<listStudentGroup.size();k++){
//                    if(predmetList.get(i).getId()==listStudentGroup.get(k).getPredmetId()){
//                        newListStudentGroup1.add(listStudentGroup.get(k));
//                    }
//                }
////                disposable.add(repository.getGradeById(listStudent.get(i).getId(),listPractice.get(j).getId())
////                        .subscribeOn(ioScheduler)
////                        .observeOn(uiScheduler)
////                        .subscribe(a    -> Log.i("1",listStudent.get(0).getId() +" "+ listPractice.get(0).getId()+" "+ a.getName()),
////                                throwable -> Log.i("1", "Unable to update username" + throwable)));
//
//
//
//            newlistStudentGroup.add(newListStudentGroup1);
//
//        }
//        return newlistStudentGroup;
//    }

}

