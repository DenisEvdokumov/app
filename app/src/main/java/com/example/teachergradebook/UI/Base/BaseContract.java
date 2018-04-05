package com.example.teachergradebook.UI.Base;

import android.os.Bundle;

/**
 * Created by Денис on 03.04.2018.
 */

public interface BaseContract {
    interface View {

    }

    interface Presenter<V extends BaseContract.View> {

        void onAttachView();

        void onDetachView();
        void onDetachLifeCycle();

        Bundle getStateBundle();

        void onPresenterDestroy();
    }
}

