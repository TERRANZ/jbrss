package ru.terra.jbrss.entity;

import android.net.Uri;
import android.provider.BaseColumns;
import ru.terra.jbrss.constants.Constants;

public interface FeedPostEntity extends BaseColumns {
	String CONTENT_DIRECTORY = "post";
	Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY + "/" + CONTENT_DIRECTORY);
	String CONTENT_TYPE = "entity.cursor.dir/post";
	String CONTENT_ITEM_TYPE = "entity.cursor.item/post";

	String POST_EXTERNAL_ID = "ext_id";
	String POST_FEED_ID = "post_feed_id";
	String POST_DATE = "post_date";
	String POST_TITLE = "post_title";
	String POST_LINK = "post_link";
	String POST_TEXT = "post_text";
	String POST_ISREAD = "post_read";
}
