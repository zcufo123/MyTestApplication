package com.test.dagger.sample;

import com.test.mytestapplication.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PersonModule.class}, dependencies = {ContextComponent.class})
public interface PersonComponent {

    void inject(MainActivity mainActivity);
}
