package ru.terra.jbrss;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(formKey = "",
        formUri = "http://terranz.ath.cx/jbrss/errors/do.error.report/jbrss",
        httpMethod = HttpSender.Method.POST,
        mode = ReportingInteractionMode.SILENT)
public class JBRSSApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }
}