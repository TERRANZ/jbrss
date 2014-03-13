package ru.terra.jbrss.constants;

import ru.terra.server.constants.CoreUrlConstants;

public class URLConstants extends CoreUrlConstants {
    public static final String SERVER_URL = "http://terranz.ath.cx/jbrss/";


    public class DoJson {
        public static final String DO_CREATE = "do.create.json";
        public static final String DO_UPDATE = "do.update.json";

        public class Login {
            public static final String LOGIN = "/login/";
            public static final String LOGIN_DO_LOGIN_JSON = "do.login.json";
            public static final String LOGIN_DO_REGISTER_JSON = "do.register.json";
            public static final String LOGIN_PARAM_USER = "user";
            public static final String LOGIN_PARAM_PASS = "pass";
            public static final String LOGIN_PARAM_CAPTCHA = "captcha";
            public static final String LOGIN_PARAM_CAPVAL = "capval";
        }

        public class Rss {
            public static final String RSS = "/rss/";
            public static final String RSS_DO_ADD = "do.add.json";
            public static final String RSS_DO_DEL = "do.del.json";
            public static final String RSS_DO_UPDATE = "do.update.json";
            public static final String RSS_DO_MARK_READ = "do.markread.json";
            public static final String RSS_DO_LIST = "do.list.json";
            public static final String RSS_DO_GET_UNREAD = "do.get.unread.json";
            public static final String RSS_DO_GET_FEED = "do.get.feed.json";
            public static final String RSS_DO_SEARCH = "do.search.json";
            public static final String RSS_PARAM_TIMESTAMP = "timestamp";
        }

        public class Captcha {
            public static final String CAPTCHA = "/captcha/";
            public static final String CAP_GET = "do.get.json";
        }

        public class ErrorReports {
            public static final String REPORT = "/errors/";
            public static final String DO_REPORT = "do.error.report";
        }
    }

    public class UI {
        public static final String UI = "/ui/";
        public static final String MAIN = "main";
        public static final String LOGIN = "login";
        public static final String REG = "reg";
        public static final String ADD = "add";

    }

    public class Resources {
        public static final String RESOURCES = "/resources/";
    }

    public class Yandex {
        public static final String PARAM_KEY = "$key";
        public static final String PARAM_CID = "$id";
        public static final String PARAM_CVAL = "$val";
        public static final String CAPTCHA_GET_URL = "http://cleanweb-api.yandex.ru/1.0/get-captcha?key="
                + PARAM_KEY;
        public static final String CAPTCHA_CHECK_URL = "http://cleanweb-api.yandex.ru/1.0/check-captcha?key="
                + PARAM_KEY + "&captcha=" + PARAM_CID + "&value=" + PARAM_CVAL;
    }

}
