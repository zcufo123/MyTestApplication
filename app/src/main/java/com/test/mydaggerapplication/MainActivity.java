package com.test.mydaggerapplication;

import android.os.Bundle;

import com.test.dagger.android.BaseActivity;
import com.test.dagger.android.DaggerMainComponent;
import com.test.dagger.android.MainComponent;
import com.test.dagger.android.MainModule;
import com.test.mytestapplication.R;

public class MainActivity extends BaseActivity {

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dagger_activity_main);

        mainComponent = DaggerMainComponent.builder()
                .activityComponent(getActivityComponent())
                .mainModule(new MainModule())
                .build();
        mainComponent.inject(this);

        getFragmentManager().beginTransaction().add(R.id.fragment_container, new MainFragment(), "main").commit();
    }

    public MainComponent getMainComponent() {
        return this.mainComponent;
    }
}
