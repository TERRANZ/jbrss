package ru.terra.jbrss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import ru.terra.jbrss.activity.FeedPostsListActivity;
import ru.terra.jbrss.activity.LoginActivity;
import ru.terra.jbrss.activity.SettingsActivity;
import ru.terra.jbrss.activity.components.AddFeedAsyncTask;
import ru.terra.jbrss.activity.components.FeedListCursorAdapter;
import ru.terra.jbrss.core.WorkIsDoneListener;
import ru.terra.jbrss.entity.FeedEntity;
import ru.terra.jbrss.network.JBRssRest;
import ru.terra.jbrss.service.CheckForOldService;
import ru.terra.jbrss.service.UpdateService;

public class MainActivity extends RoboActivity {

    @Inject
    private JBRssRest restService;

    @InjectView(R.id.lv_feeds)
    private ListView lvFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        startActivity(new Intent(this, LoginActivity.class));
        Cursor feedsCursor = getContentResolver().query(FeedEntity.CONTENT_URI, null, null, null, null);
        lvFeeds.setAdapter(new FeedListCursorAdapter(this, feedsCursor));
        lvFeeds.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, FeedPostsListActivity.class).putExtra("id", ((Integer) (view).getTag())));
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                startService(new Intent(MainActivity.this, CheckForOldService.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_main_refresh: {
                startService(new Intent(this, UpdateService.class));
                return true;
            }
            case R.id.mi_main_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            case R.id.mi_main_add: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Новый фид");

                // Set up the input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFeed(input);
                    }
                });
                builder.show();
                return true;
            }
        }
        return true;
    }

    private void addFeed(EditText input) {
        new AddFeedAsyncTask(MainActivity.this, restService, new WorkIsDoneListener() {
            @Override
            public void workIsDone(int action, Exception exception, String... params) {
                if (Boolean.parseBoolean(params[0]))
                    startService(new Intent(MainActivity.this, UpdateService.class));
            }
        }).execute(input.getText().toString());
    }

}
