package com.example.teachergradebook.UI.PractieGroupList;

import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.Res;
import com.example.teachergradebook.data.model.ResList;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;

import java.security.acl.Group;
import java.util.List;

/**
 * Created by Денис on 17.03.2018.
 */

public interface PGContract extends BaseContract{

    interface View extends BaseContract.View {
        void showTable(List<Predmet> presenters);

        void showErrorMessage(String error);


        void clearQuestions();

        void showNoDataMessage();

        void stopLoadingIndicator();


        void showError(String message);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void loadTable(boolean onlineRequired,String token,Long userId);
        void logout(String token);

        void login(String login, String password);
    }
}
