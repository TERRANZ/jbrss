package ru.terra.jbrss;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import ru.terra.jbrss.constants.URLConstants;

@ReportsCrashes(formKey = "",
        formUri = URLConstants.SERVER_URL + URLConstants.DoJson.ErrorReports.REPORT + URLConstants.DoJson.ErrorReports.DO_REPORT,
        httpMethod = HttpSender.Method.POST,
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.error_caught)
public class JBRSSApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }
}
