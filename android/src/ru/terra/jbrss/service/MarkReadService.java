package ru.terra.jbrss.service;

import roboguice.service.RoboIntentService;
import ru.terra.jbrss.constants.Constants;
import ru.terra.jbrss.entity.FeedEntity;
import ru.terra.jbrss.entity.FeedPostEntity;
import ru.terra.jbrss.network.JBRssRest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import com.google.inject.Inject;
import ru.terra.jbrss.network.UnableToLoginException;

import java.io.IOException;

public class MarkReadService extends RoboIntentService {

	public MarkReadService() {
		super("Сервис меток");
	}

	@Inject
	private JBRssRest jbRssRest;

	@Override
	protected void onHandleIntent(Intent intent) {
		int operation = intent.getIntExtra("operation", -1);
		switch (operation) {
		case Constants.OP_MARK_READ_POST: {
            try {
                jbRssRest.markReadPost(intent.getIntExtra("id", 0));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnableToLoginException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
			break;
		case Constants.OP_MARK_READ_FEED: {
            try {
                jbRssRest.markReadFeed(intent.getIntExtra("id", 0));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnableToLoginException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
			break;
		}
		updateUnreads();
	}

	private void updateUnreads() {
		Cursor feeds = null;
		try {
			feeds = getContentResolver().query(FeedEntity.CONTENT_URI, new String[] { FeedEntity.FEED_EXTERNAL_ID }, null, null, null);
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
			feeds = getContentResolver().query(FeedEntity.CONTENT_URI, new String[] { FeedEntity.FEED_EXTERNAL_ID },
					FeedEntity.FEED_EXTERNAL_ID + " <> ?", new String[] { Constants.ALL_FEED_POSTS_ITEM_ID.toString() }, null);
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
		getContentResolver().update(FeedEntity.CONTENT_URI, cvFeed, FeedEntity.FEED_EXTERNAL_ID + " = ?", new String[] { feedId.toString() });
	}

	private Integer getUnreads(Integer feedId) {
		Cursor posts = null;
		try {
			if (feedId >= 0)
				posts = getContentResolver().query(FeedPostEntity.CONTENT_URI, null,
						FeedPostEntity.POST_FEED_ID + " = ? AND " + FeedPostEntity.POST_ISREAD + " = ?", new String[] { feedId.toString(), "false" },
						null);
			else
				posts = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_ISREAD + " = ?", new String[] { "false" },
						null);
			return posts.getCount();
		} finally {
			if (posts != null)
				posts.close();
		}
	}

}
