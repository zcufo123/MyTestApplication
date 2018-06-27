package com.test.dagger.android;

public class UserRepository  {

    public class User {
        public String name;
    }

    public UserRepository() {
    }

    public User getUser() {
        User user = new User();
        user.name = "yxm";
        return user;
    }
}
