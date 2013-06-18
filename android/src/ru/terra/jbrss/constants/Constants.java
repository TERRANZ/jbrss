package ru.terra.jbrss.constants;

public interface Constants {
	public static final String CONFIG_SESSION = "sessionid";
	public static final String CONFIG_LAST_UPDATE_TIME = "last_update_time";
	public static final int LOGIN_ACTION = 0;
	public static final int ADD_FEED_ACTION = 1;
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "et";
	public static final String AUTHORITY = "ru.terra.jbrss.entity";

	public static final int OP_MARK_READ_POST = 1;
	public static final int OP_MARK_READ_FEED = 2;
	public static final int OP_MARK_READ_ALL = 3;

	public static final Integer ALL_FEED_POSTS_ITEM_ID = -1;
}
