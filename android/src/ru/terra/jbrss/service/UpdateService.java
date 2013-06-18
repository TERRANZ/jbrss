package ru.terra.jbrss.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import com.google.inject.Inject;
import org.acra.ACRA;
import roboguice.service.RoboIntentService;
import ru.terra.jbrss.R;
import ru.terra.jbrss.activity.FeedPostsListActivity;
import ru.terra.jbrss.constants.Constants;
import ru.terra.jbrss.core.SettingsService;
import ru.terra.jbrss.core.helper.Logger;
import ru.terra.jbrss.dto.CommonDTO;
import ru.terra.jbrss.dto.rss.*;
import ru.terra.jbrss.entity.FeedEntity;
import ru.terra.jbrss.entity.FeedPostEntity;
import ru.terra.jbrss.network.JBRssRest;

import java.util.Date;
import java.util.List;

public class UpdateService extends RoboIntentService {

    private NotificationManager notifier;
    private NotificationCompat.Builder builder;

    @Inject
    public UpdateService() {
        super("Сервис обновлений");
    }

    @Inject
    private JBRssRest jbRssRest;
    @Inject
    private SettingsService settingsService;

    @Override
    protected void onHandleIntent(Intent intent) {
        notifyStartUpdate();
        try {
            SimpleIntDataDTO updateDTO = jbRssRest.update();
            if (updateDTO != null) {
                if (!checkError(updateDTO)) {
                    Integer updated = updateDTO.data;
                    if (updated != -1) {
                        Logger.i("UpdateService", updated + " new records");

                        ListFeedDTO fdto = jbRssRest.getFeeds();
                        if (fdto != null && !checkError(fdto)) {
                            List<FeedDTO> feeds = fdto.data;
                            if (feeds != null && feeds.size() > 0) {
                                for (FeedDTO dto : feeds) {
                                    if (!checkFeedExists(dto.id)) {
                                        ContentValues cv = new ContentValues();
                                        cv.put(FeedEntity.FEED_EXTERNAL_ID, dto.id);
                                        cv.put(FeedEntity.FEED_NAME, dto.feedname);
                                        cv.put(FeedEntity.FEED_UPDATED, dto.updateTime);
                                        cv.put(FeedEntity.FEED_URL, dto.feedurl);
                                        getContentResolver().insert(FeedEntity.CONTENT_URI, cv);
                                    }
                                }
                            }

                            ListFeedPostDTO pdto = jbRssRest.getUnreadPosts();
                            if (!checkError(pdto)) {
                                if (pdto != null) {
                                    List<FeedPostDTO> posts = pdto.data;
                                    if (posts != null && posts.size() > 0) {
                                        for (FeedPostDTO post : posts) {
                                            if (!checkPostExists(post.id)) {
                                                ContentValues cv = new ContentValues();
                                                cv.put(FeedPostEntity.POST_DATE, post.postdate);
                                                cv.put(FeedPostEntity.POST_EXTERNAL_ID, post.id);
                                                cv.put(FeedPostEntity.POST_LINK, post.postlink);
                                                cv.put(FeedPostEntity.POST_FEED_ID, post.feedId);
                                                cv.put(FeedPostEntity.POST_TEXT, post.posttext);
                                                cv.put(FeedPostEntity.POST_TITLE, post.posttitle);
                                                cv.put(FeedPostEntity.POST_ISREAD, String.valueOf(post.isRead));
                                                getContentResolver().insert(FeedPostEntity.CONTENT_URI, cv);
                                            }
                                        }
                                    }
                                    checkForOld();
                                    updateUnreads();
                                }
                            }
                            notify("Новых сообщений: " + updated, true);

                        } else {
                            notify("Новый сообщений нет", false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
        }
    }

    private boolean checkError(CommonDTO dto) {
        if (dto.errorCode > 0) {
            Toast.makeText(this, dto.errorMessage, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void updateUnreads() {
        Cursor feeds = null;
        try {
            feeds = getContentResolver().query(FeedEntity.CONTENT_URI, new String[]{FeedEntity.FEED_EXTERNAL_ID}, null, null, null);
            if (feeds.moveToFirst()) {
                Integer id = feeds.getInt(feeds.getColumnIndex(FeedEntity.FEED_EXTERNAL_ID));
                writeUnread(id, getUnreads(id));
                while (feeds.moveToNext()) {
                    id = feeds.getInt(feeds.getColumnIndex(FeedEntity.FEED_EXTERNAL_ID));
                    writeUnread(id, getUnreads(id));
                }
            }
            Integer allUnread = 0;
            feeds.close();
            feeds = getContentResolver().query(FeedEntity.CONTENT_URI, new String[]{FeedEntity.FEED_EXTERNAL_ID},
                    FeedEntity.FEED_EXTERNAL_ID + " <> ?", new String[]{Constants.ALL_FEED_POSTS_ITEM_ID.toString()}, null);
            if (feeds.moveToFirst()) {
                allUnread += getUnreads(feeds.getInt(feeds.getColumnIndex(FeedEntity.FEED_EXTERNAL_ID)));
                while (feeds.moveToNext())
                    allUnread += getUnreads(feeds.getInt(feeds.getColumnIndex(FeedEntity.FEED_EXTERNAL_ID)));
            }
            writeUnread(Constants.ALL_FEED_POSTS_ITEM_ID, allUnread);
        } finally {
            if (feeds != null)
                feeds.close();
        }
    }

    private void writeUnread(Integer feedId, Integer unread) {
        ContentValues cvFeed = new ContentValues();
        cvFeed.put(FeedEntity.FEED_UNREAD, unread);
        getContentResolver().update(FeedEntity.CONTENT_URI, cvFeed, FeedEntity.FEED_EXTERNAL_ID + " = ?", new String[]{feedId.toString()});
    }

    private Integer getUnreads(Integer feedId) {
        Cursor posts = null;
        try {
            if (feedId >= 0)
                posts = getContentResolver().query(FeedPostEntity.CONTENT_URI, null,
                        FeedPostEntity.POST_FEED_ID + " = ? AND " + FeedPostEntity.POST_ISREAD + " = ?", new String[]{feedId.toString(), "false"},
                        null);
            else
                posts = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_ISREAD + " = ?", new String[]{"false"},
                        null);
            return posts.getCount();
        } finally {
            if (posts != null)
                posts.close();
        }
    }

    private boolean checkFeedExists(Integer id) {
        Cursor c = null;
        try {
            c = getContentResolver().query(FeedEntity.CONTENT_URI, null, FeedEntity.FEED_EXTERNAL_ID + " = ?", new String[]{id.toString()}, null);
            if (c != null)
                if (c.moveToFirst())
                    return true;
                else
                    return false;
        } finally {
            if (c != null)
                c.close();
        }
        return false;
    }

    private boolean checkPostExists(Integer id) {
        Cursor c = null;
        try {
            c = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_EXTERNAL_ID + " = ?",
                    new String[]{id.toString()}, null);
            if (c != null)
                if (c.moveToFirst())
                    return true;
                else
                    return false;
        } finally {
            if (c != null)
                c.close();
        }
        return false;
    }

    private void notify(String text, boolean showList) {
        notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.info_selected);
        builder.setContentText(text);
        builder.setContentTitle(text);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setTicker(text);
        if (showList) {
            Intent intent = new Intent(this, FeedPostsListActivity.class);
            intent.putExtra("from_date", settingsService.getSetting("last_sync_time", "0"));
            settingsService.saveSetting("last_sync_time", String.valueOf(new Date().getTime()));
            intent.putExtra("from_notify", true);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pIntent);
        }
        notifier.notify(0, builder.build());
    }

    private void notifyStartUpdate() {
        notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.info_selected);
        builder.setContentText("Обновление...");
        builder.setContentTitle("Обновление...");
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setTicker("Обновление...");
        builder.setProgress(100, 100, true);
        notifier.notify(0, builder.build());
    }

    private void checkForOld() {
        Integer days = Integer.valueOf(settingsService.getSetting(getString(R.string.estimate_days), "0"));
        if (days > 0) {
            Long d = 0L;
            int dayInMs = 1000 * 60 * 60 * (24 * days);
            Date previousDay = new Date(new Date().getTime() - dayInMs);
            d = previousDay.getTime();
            getContentResolver().delete(FeedPostEntity.CONTENT_URI, FeedPostEntity.POST_DATE + " < ? AND " + FeedPostEntity.POST_ISREAD + " = ?", new String[]{d.toString(), "true"});
        }
    }
}
