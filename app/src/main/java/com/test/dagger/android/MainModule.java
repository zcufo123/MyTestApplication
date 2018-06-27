package com.test.dagger.android;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    public UserRepository provideUserRepository() {
        return new UserRepository();
    }
}
