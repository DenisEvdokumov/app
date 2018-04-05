package com.example.teachergradebook.UI.Table;

import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.UI.Base.BasePresenter;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;

import java.security.acl.Group;
import java.util.List;

/**
 * Created by Денис on 17.03.2018.
 */

public interface TableContract extends BaseContract{

    interface View extends BaseContract.View {
        void showGroup(List<Group> groups);

        void showStudents(List<Student> students);

        void showPractice(List<Practice> practices);



        void showTable(List<Student> students, List<Practice> practices, List<List<Grade>> grades);

        void clearQuestions();

        void showNoDataMessage();

        void showErrorMessage(String error);



        void stopLoadingIndicator();

        void showEmptySearchResult();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void loadTable(boolean onlineRequired);

//        void getQuestion(long questionId);


        void deleteGroup(Group group);

        void addGrade(String grade);

        void addStudent();
        void addPractice();
    }
}
