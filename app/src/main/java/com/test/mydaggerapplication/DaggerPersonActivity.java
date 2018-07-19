package com.test.mydaggerapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.test.dagger.sample.ContextComponent;
import com.test.dagger.sample.ContextModule;
import com.test.dagger.sample.DaggerContextComponent;
import com.test.dagger.sample.DaggerPersonComponent;
import com.test.dagger.sample.Person;
import com.test.dagger.sample.PersonComponent;
import com.test.dagger.sample.PersonModule;
import com.test.dagger.sample.PersonWithContext;
import com.test.dagger.sample.PersonWithName;
import com.test.mytestapplication.R;

import javax.inject.Inject;

public class DaggerPersonActivity extends AppCompatActivity {

    public static final String TAG = "DaggerPersonActivity";

    @PersonWithContext
    @Inject
    Person person1;

    @PersonWithName
    @Inject
    Person person2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ContextComponent contextComponent = DaggerContextComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        PersonComponent component = DaggerPersonComponent.builder()
                .contextComponent(contextComponent)
                .personModule(new PersonModule()).build();
        component.inject(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Snackbar.make(view, "Replace with your own action on DaggerPersonActivity", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
