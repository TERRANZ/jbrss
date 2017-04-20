package ru.terra.jbrss.shared.constants;

/**
 * Created by terranz on 13.04.17.
 */
public class URLConstants {
    public static class Account {
        public static final String USER = "user";
        public static final String GET_ID = "/{client}/id";

        public static final String ALL_IDS = "/ids";
        public static final String CREATE = "/create";
        public static final String LOGIN = "/login";
    }

    public static class Rss {
        public static final String FEED = "/feed";
        public static final String FEED_POSTS = "/{fid}/list";
        public static final String ADD = "/add";
        public static final String DEL = "/{fid}/del";
        public static final String UPDATE = "/update";
        public static final String SETTINGS = "/settings";
    }
}
