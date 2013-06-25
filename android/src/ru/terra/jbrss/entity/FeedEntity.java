package ru.terra.jbrss.entity;

import android.net.Uri;
import android.provider.BaseColumns;
import ru.terra.jbrss.constants.Constants;

public interface FeedEntity extends BaseColumns {
	String CONTENT_DIRECTORY = "feed";
	Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY + "/" + CONTENT_DIRECTORY);
	String CONTENT_TYPE = "entity.cursor.dir/feed";
	String CONTENT_ITEM_TYPE = "entity.cursor.item/feed";

	String FEED_NAME = "feed_name";
	String FEED_UNREAD = "feed_unread";
	String FEED_URL = "feed_url";
	String FEED_UPDATED = "feed_updated";
	String FEED_EXTERNAL_ID = "ext_id";
}
