package com.test.dagger.sample;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonModule {

//    @Named("context")
    @Singleton
    @Provides
    @PersonWithContext
    public Person providesPersonWithContext(Context context) {
        System.out.println("a person created from PersonModule by using Context");
        return new Person(context);
    }

//    @Named("string")
    @Singleton
    @Provides
    @PersonWithName
    public Person providesPersonWithName() {
        System.out.println("a person created from PersonModule by using String");
        return new Person("example");
    }
}