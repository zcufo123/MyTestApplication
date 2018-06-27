package com.test.mytestapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.test.butterknife.BindView;
import com.test.butterknife.ContentView;
import com.test.butterknife.OnClick;
import com.test.dagger.sample.ContextComponent;
import com.test.dagger.sample.ContextModule;
import com.test.dagger.sample.DaggerContextComponent;
import com.test.dagger.sample.DaggerPersonComponent;
import com.test.dagger.sample.PersonComponent;
import com.test.dagger.sample.PersonModule;
import com.test.dagger.sample.Person;
import com.test.dagger.sample.PersonWithContext;
import com.test.dagger.sample.PersonWithName;

import javax.inject.Inject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolBar;

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
                .mainModule(new PersonModule()).build();
        component.inject(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    @OnClick({R.id.toolbar})
    public void onClick(View view) {
        Log.d("Test", "view is " + view);
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
