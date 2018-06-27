package com.test.dagger.sample;

import android.content.Context;

public class Person {

    public Person(Context context) {
        System.out.println("a person created with Context");
        System.out.println("This (Context) is " + this);
    }

    public Person(String string) {
        System.out.println("a person created with String");
        System.out.println("This (String) is " + this);
    }

}
