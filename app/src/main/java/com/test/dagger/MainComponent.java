package com.test.dagger;

import com.test.mytestapplication.MainActivity;

import dagger.Component;

@Component(modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
