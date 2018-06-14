package com.example.teachergradebook.UI.PractieGroupList;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.util.Log;

import com.example.teachergradebook.UI.Base.BasePresenter;

import com.example.teachergradebook.data.model.Predmet;

import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;
import com.example.teachergradebook.data.repository.TableRepository;
import com.example.teachergradebook.util.schedulers.RunOn;

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
                                                                    view.showTable(a.getData());

                                                                },
                                                                throwable -> view.showErrorMessage(throwable.getMessage())));
                                    },
                                    throwable -> view.showErrorMessage(throwable.getMessage())));


        }else {
            disposable.add(
                    repository.loadPredmetOFline(onlineRequired, token, userId)
                            .subscribeOn(ioScheduler)
                            .observeOn(uiScheduler)

                            .subscribe((a) -> view.showTable(a),
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

