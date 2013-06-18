package ru.terra.jbrss.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import ru.terra.jbrss.R;
import ru.terra.jbrss.activity.components.DownloadImageAsyncTask;
import ru.terra.jbrss.activity.components.RegAsyncTask;
import ru.terra.jbrss.core.AsyncTaskEx;
import ru.terra.jbrss.core.SettingsService;
import ru.terra.jbrss.core.WorkIsDoneListener;
import ru.terra.jbrss.dto.captcha.CaptchDTO;
import ru.terra.jbrss.network.JBRssRest;

public class RegActivity extends RoboActivity {
    @Inject
    private SettingsService settingsService;
    @Inject
    private JBRssRest jbRssRest;
    @InjectView(R.id.etUserName)
    private EditText etUserName;
    @InjectView(R.id.edtPassword)
    private EditText etPassword;
    @InjectView(R.id.etCaptcha)
    private EditText etCaptcha;
    @InjectView(R.id.ivCaptcha)
    private ImageView ivCaptcha;

    private CaptchDTO captcha;

    public class CaptchaLoadAsyncTask extends AsyncTaskEx<Void, Void, CaptchDTO> {

        public CaptchaLoadAsyncTask() {
            super(20000l, RegActivity.this);
        }

        @Override
        protected CaptchDTO doInBackground(Void... params) {
            try {
                return jbRssRest.getCaptcha();
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(CaptchDTO result) {
            captcha = result;
            new DownloadImageAsyncTask(ivCaptcha).execute(captcha.image);
        }

        @Override
        protected void onCancelled() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_reg);
        new CaptchaLoadAsyncTask().execute();
    }

    public void refreshCaptcha(View v) {
        new CaptchaLoadAsyncTask().execute();
    }

    public void reg(View v) {
        new RegAsyncTask(jbRssRest, new WorkIsDoneListener() {

            @Override
            public void workIsDone(int action, Exception e, String... params) {
                Boolean res = Boolean.parseBoolean(params[0]);
                if (res) {
                    setResult(RESULT_OK);
                    settingsService.saveSetting(getString(R.string.username), etUserName.getText().toString());
                    settingsService.saveSetting(getString(R.string.password), etPassword.getText().toString());
                    finish();
                } else {
                    Toast.makeText(RegActivity.this, "Регистрация не прошла", Toast.LENGTH_SHORT).show();
                }
            }
        }, this).execute(etUserName.getText().toString(), etPassword.getText().toString(), captcha.cid, etCaptcha.getText().toString());
    }
}
