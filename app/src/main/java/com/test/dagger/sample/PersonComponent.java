package com.test.dagger.sample;

import com.test.mydaggerapplication.DaggerPersonActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PersonModule.class}, dependencies = {ContextComponent.class})
public interface PersonComponent {

    void inject(DaggerPersonActivity mainActivity);
}
