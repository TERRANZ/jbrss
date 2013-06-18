package ru.terra.jbrss.activity.components;

import android.content.Context;
import android.widget.Toast;
import ru.terra.jbrss.constants.Constants;
import ru.terra.jbrss.core.AsyncTaskEx;
import ru.terra.jbrss.core.WorkIsDoneListener;
import ru.terra.jbrss.dto.rss.SimpleBooleanDataDTO;
import ru.terra.jbrss.network.JBRssRest;

public class AddFeedAsyncTask extends AsyncTaskEx<String, Void, Boolean> {

    private JBRssRest jbRssRest;
    private WorkIsDoneListener workIsDoneListener;
    private String errorMessage = "";
    private Context context;

    public AddFeedAsyncTask(Context context, JBRssRest jbRssRest, WorkIsDoneListener workIsDoneListener) {
        super(20000l, context);
        this.context = context;
        this.jbRssRest = jbRssRest;
        this.workIsDoneListener = workIsDoneListener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        SimpleBooleanDataDTO ret = null;
        try {
            ret = jbRssRest.addFeed(params[0]);
        } catch (Exception e) {
            exception = e;
        }
        if (ret != null) {
            if (ret.errorCode > 0) {
                errorMessage = ret.errorMessage;
            }
            return ret.data;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (errorMessage.length() > 0)
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();

        if (workIsDoneListener != null)
            workIsDoneListener.workIsDone(Constants.ADD_FEED_ACTION, exception, result.toString());
    }

    @Override
    protected void onCancelled() {
    }
}
