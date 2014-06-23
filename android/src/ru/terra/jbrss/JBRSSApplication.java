package ru.terra.jbrss;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "",
        formUri = "http://terranz.ath.cx/jbrss/errors/do.error.report",
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.error_caught)
public class JBRSSApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }
}