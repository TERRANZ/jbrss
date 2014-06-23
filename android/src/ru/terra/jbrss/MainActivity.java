package ru.terra.jbrss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                activity.setProgressBarIndeterminateVisibility(false);
                cookie = CookieManager.getInstance().getCookie(url);
            }
        });
        webView.addJavascriptInterface(new ShareAction(this), "Android");

    }
}
