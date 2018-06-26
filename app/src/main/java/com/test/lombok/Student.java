package com.test.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Student {

    public enum Gender {
        MALE,
        FEMALE
    }

    private String name;
    private int age;
    private Gender gender;


    public void takeClass(@NonNull String place) {
    }
}
