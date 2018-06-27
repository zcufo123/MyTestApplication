package com.test.mydaggerapplication;

import android.os.Bundle;

import com.test.dagger.android.BaseActivity;
import com.test.dagger.android.MainComponent;
import com.test.dagger.android.MainModule;
import com.test.mytestapplication.R;

public class MainActivity extends BaseActivity {

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainComponent = DaggerMainComponent.builder()
                .activityComponent(getActivityComponent())
                .mainModule(new MainModule())
                .build();
        mainComponent.inject(this);
    }

    public MainComponent getMainComponent() {
        return this.mainComponent;
    }
}
