package com.example.teachergradebook.UI.Base;

import android.arch.lifecycle.ViewModel;

/**
 * Created by Денис on 03.04.2018.
 */

public final class BaseViewModel<V extends BaseContract.View,P extends BaseContract.Presenter<V>>
        extends ViewModel{
    private P presenter;

    void setPresenter(P presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    P getPresenter() {
        return this.presenter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        presenter.onPresenterDestroy();
        presenter = null;
    }


}
