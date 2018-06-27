package com.test.mydaggerapplication;

import com.test.dagger.android.UserRepository;

import javax.inject.Inject;

public class MainFragmentContract {
    public interface View {
        void setUserName(String name);

        void showToast(String msg);
    }

    public static class Presenter {
        public UserRepository userRepository;

        @Inject
        public Presenter(UserRepository repository) {
            this.userRepository = repository;
        }

        private View view;

        public void setView(View view) {
            this.view = view;
        }

        public void toastButtonClick() {
            String msg = "hello world";
            view.showToast(msg);
        }

        public void userInfoButtonClick() {
            UserRepository.User userData = this.userRepository.getUser();
            this.view.setUserName((userData.name));
        }
    }
}
