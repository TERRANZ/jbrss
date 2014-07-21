package ru.terra.jbrss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class MainActivity extends Activity {
    private WebView webView;
    private String cookie;
    private View actionBar;
    private int baseHeight;
    private int webViewOnTouchHeight;
    private int barHeight;
    private float heightChange;
    private float startEventY;
    private View layout;
    private TextView tvUrl;

    public class ShareAction {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        ShareAction(Context c) {
            mContext = c;
        }

        public void share(String id) {
            Toast.makeText(mContext, "Шаринг", Toast.LENGTH_LONG).show();
            new AsyncTask<Integer, Void, Void>() {
                private ProgressDialog progressDialog;
                private String title;
                private String text;
                private String link;

                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(mContext, "Загрузка", "Получение поста");
                }

                @Override
                protected Void doInBackground(Integer... id) {

                    try {
                        URL url = new URL("http://terranz.ath.cx/jbrss/rss/do.get.post.json?id=" + id[0]);
                        URLConnection urlConn = url.openConnection();
                        urlConn.setRequestProperty("Cookie", cookie);
                        urlConn.connect();
                        Scanner s = new Scanner(urlConn.getInputStream());
                        JSONObject post = new JSONObject(s.useDelimiter("\\A").next());
                        title = post.getString("posttitle");
                        text = post.getString("posttext");
                        link = post.getString("postlink");
                        return null;
                    } catch (IOException ex) {
                        ex.printStackTrace(); // for now, simply output it.
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                    }


                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

                    // set the type
                    shareIntent.setType("text/plain");

                    // add a subject
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);

                    // build the body of the message to be shared
                    String shareMessage = title;
                    shareMessage += "\n";
                    shareMessage += link;
                    shareMessage += "\n";
                    shareMessage += text;
                    // add the message
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

                    // start the chooser for sharing
                    startActivity(Intent.createChooser(shareIntent, "Выберите приложение"));
                }
            }.execute(Integer.parseInt(id));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
        setContentView(R.layout.a_main);
        actionBar = findViewById(R.id.actionBar);
        layout = findViewById(R.id.rl);
        tvUrl = (TextView) findViewById(R.id.tvUrl);
        final Activity activity = this;

        webView = (WebView) findViewById(R.id.wvMain);
        webView.loadUrl("http://terranz.ath.cx/jbrss/ui/main");
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.contains("terranz.ath.cx")) {
                    view.loadUrl(url);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                activity.setProgressBarIndeterminateVisibility(false);
                cookie = CookieManager.getInstance().getCookie(url);
                tvUrl.setText(url);
            }
        });
        webView.addJavascriptInterface(new ShareAction(this), "Android");
        webView.setOnTouchListener(listener);
        webView.post(new Runnable() {
            @Override
            public void run() {
                barHeight = actionBar.getMeasuredHeight();
                baseHeight = layout.getMeasuredHeight();
                webView.getLayoutParams().height = webView.getMeasuredHeight() - barHeight;
                webView.requestLayout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean resizeView(float delta) {
        heightChange = delta;
        int newHeight = (int) (webViewOnTouchHeight - delta);
        if (newHeight > baseHeight) { // scroll over top
            if (webView.getLayoutParams().height < baseHeight) {
                webView.getLayoutParams().height = baseHeight;
                webView.requestLayout();
                return true;
            }
        } else if (newHeight < baseHeight - barHeight) { // scroll below bar
            if (webView.getLayoutParams().height > baseHeight - barHeight) {
                webView.getLayoutParams().height = baseHeight - barHeight;
                webView.requestLayout();
                return true;
            }
        } else { // scroll between top and bar
            webView.getLayoutParams().height = (int) (webViewOnTouchHeight - delta);
            webView.requestLayout();
            return true;
        }
        return false;
    }

    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    startEventY = event.getY();
                    heightChange = 0;
                    webViewOnTouchHeight = webView.getLayoutParams().height;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float delta = (event.getY() + heightChange) - startEventY;
                    boolean heigthChanged = resizeView(delta);
                    if (heigthChanged) {
                        actionBar.setTranslationY(baseHeight - webView.getLayoutParams().height - barHeight);
                    }
            }
            return false;
        }
    };

    public void refresh(View view) {

    }
}
