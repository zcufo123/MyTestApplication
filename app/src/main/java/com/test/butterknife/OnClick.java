package com.test.butterknife;

import android.view.View;

import com.test.butterknife.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)

@Retention(RetentionPolicy.RUNTIME)

@EventType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener",

        methodName = "onClick")

public @interface OnClick {

    int[] value();

}
