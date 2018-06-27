package com.test.dagger.sample;

import android.content.Context;

import dagger.Component;

@Component(modules = {ContextModule.class})
public interface ContextComponent {
    Context getContext();
}
