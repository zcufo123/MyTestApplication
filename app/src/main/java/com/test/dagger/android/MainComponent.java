package com.test.dagger.android;

import com.test.mydaggerapplication.MainActivity;

import dagger.Component;

@MainActivityScope
@Component(dependencies = {ActivityComponent.class}, modules = {MainModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);

    MainFragmentComponent mainFragmentComponent();
}
