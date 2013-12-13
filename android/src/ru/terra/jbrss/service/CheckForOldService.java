package ru.terra.jbrss.service;

import android.content.Intent;
import android.util.Log;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;
import ru.terra.jbrss.R;
import ru.terra.jbrss.core.SettingsService;
import ru.terra.jbrss.entity.FeedPostEntity;

import java.util.Date;

/**
 * Date: 13.12.13
 * Time: 16:50
 */
public class CheckForOldService extends RoboIntentService {

    @Inject
    private SettingsService settingsService;

    public CheckForOldService() {
        super("Сервис удаления устаревших лент");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("CheckForOldService", "Starting...");
        Integer days = Integer.valueOf(settingsService.getSetting(getString(R.string.estimate_days), "0"));
        if (days > 0) {
            Long d = 0L;
            int dayInMs = 1000 * 60 * 60 * (24 * days);
            Date previousDay = new Date(new Date().getTime() - dayInMs);
            d = previousDay.getTime();
            int removed = getContentResolver().delete(
                    FeedPostEntity.CONTENT_URI,
                    FeedPostEntity.POST_DATE + " < ? AND " + FeedPostEntity.POST_ISREAD + " = ?",
                    new String[]{d.toString(), "1"}
            );
            Log.i("CheckForOldService", "Removed old posts: " + removed);
        }

    }
}
