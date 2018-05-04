package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.activities.MyPageActivity;
import com.travlog.android.apps.viewmodels.errors.MyPageViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.MyPageViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.MyPageViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MyPageViewModel extends ActivityViewModel<MyPageActivity>
        implements MyPageViewModelInputs, MyPageViewModelOutputs, MyPageViewModelErrors {

    public MyPageViewModel(final @NonNull Environment environment) {
        super(environment);

        environment.currentUser.loggedInUser()
                .map(user -> user.name)
                .compose(bindToLifecycle())
                .subscribe(setUsernameText);
    }

    public final MyPageViewModelInputs inputs = this;
    public final MyPageViewModelOutputs outputs = this;
    public final MyPageViewModelErrors errors = this;

    private final BehaviorSubject<String> setUsernameText = BehaviorSubject.create();

    @NonNull
    @Override
    public Observable<String> setUsernameText() {
        return setUsernameText;
    }
}
