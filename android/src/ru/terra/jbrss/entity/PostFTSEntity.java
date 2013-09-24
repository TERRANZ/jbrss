package ru.terra.jbrss.entity;

import android.net.Uri;
import ru.terra.jbrss.constants.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 24.09.13
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public interface PostFTSEntity {
    String CONTENT_DIRECTORY = "post_fts";
    Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY + "/" + CONTENT_DIRECTORY);
    String CONTENT_TYPE = "entity.cursor.dir/post_fts";
    String CONTENT_ITEM_TYPE = "entity.cursor.item/post_fts";

    String POST_EXTERNAL_ID = "ext_id";
    String POST_TEXT = "post_text";
}
