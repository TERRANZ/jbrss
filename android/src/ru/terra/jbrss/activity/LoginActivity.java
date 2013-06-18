package ru.terra.jbrss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import ru.terra.jbrss.R;
import ru.terra.jbrss.activity.components.LoginAsyncTask;
import ru.terra.jbrss.core.SettingsService;
import ru.terra.jbrss.core.WorkIsDoneListener;
import ru.terra.jbrss.network.JBRssRest;

public class LoginActivity extends RoboActivity {

    public static final String PARAM_REG_RES = "reg_res";

    @InjectView(R.id.etUserName)
    private EditText etUserName;
    @InjectView(R.id.edtPassword)
    private EditText etPassword;
    @Inject
    private SettingsService settingsService;
    @Inject
    private JBRssRest jbRssRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login);
        etUserName.setText(settingsService.getSetting(getString(R.string.username), ""));
        etPassword.setText(settingsService.getSetting(getString(R.string.password), ""));
        if (!settingsService.getSetting("logged_in", "false").equals("false")) {
            doLogin();
        }
    }

    public void login(View v) {
        settingsService.saveSetting(getString(R.string.username), etUserName.getText().toString());
        settingsService.saveSetting(getString(R.string.password), etPassword.getText().toString());
        doLogin();
    }

    private void doLogin() {
        new LoginAsyncTask(jbRssRest, new WorkIsDoneListener() {

            @Override
            public void workIsDone(int action, Exception e, String... params) {
                Boolean result = Boolean.valueOf(params[0]);
                if (result) {
                    Toast.makeText(LoginActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                    settingsService.saveSetting("logged_in", "true");
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Логин/пароль не опознаны", Toast.LENGTH_SHORT).show();
                    settingsService.saveSetting(getString(R.string.username), "");
                    settingsService.saveSetting(getString(R.string.password), "");
                }
            }
        }, this).execute();
    }

    public void reg(View v) {
        startActivityForResult(new Intent(this, RegActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            doLogin();
        }
    }

}
