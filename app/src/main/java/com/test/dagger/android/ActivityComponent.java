package com.test.dagger.android;

import android.app.Activity;

import dagger.Component;

@PerActivity
@Component(modules = {ActivityModule.class}, dependencies = {AppComponent.class})
public interface ActivityComponent {

    Activity getActivity();

    ToastUtil getToastUtil();
}
