package com.test.dagger.android;

import com.test.mydaggerapplication.MainFragment;

import dagger.Subcomponent;

@MainActivityScope
@Subcomponent
public interface MainFragmentComponent {
    void inject(MainFragment mainFragment);
}
