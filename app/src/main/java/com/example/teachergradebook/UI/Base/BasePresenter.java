package com.example.teachergradebook.UI.Base;

import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.annotation.CallSuper;

/**
 * Created by Денис on 06.03.2018.
 */

public abstract class BasePresenter<V extends BaseContract.View> implements LifecycleObserver,
        BaseContract.Presenter<V>{
    private Bundle stateBundle;

    @Override
    final public Bundle getStateBundle() {
        return stateBundle == null
                ? stateBundle = new Bundle()
                : stateBundle;
    }

    @CallSuper
    @Override
    public void onPresenterDestroy() {
        if (stateBundle != null && !stateBundle.isEmpty()) {
            stateBundle.clear();
        }
    }

}
