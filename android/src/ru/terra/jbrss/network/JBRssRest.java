package ru.terra.jbrss.network;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import org.apache.http.message.BasicNameValuePair;
import roboguice.inject.ContextSingleton;
import ru.terra.jbrss.R;
import ru.terra.jbrss.constants.Constants;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.core.SettingsService;
import ru.terra.jbrss.dto.captcha.CaptchDTO;
import ru.terra.jbrss.dto.rss.ListFeedDTO;
import ru.terra.jbrss.dto.rss.ListFeedPostDTO;
import ru.terra.jbrss.dto.rss.SimpleBooleanDataDTO;
import ru.terra.jbrss.dto.rss.SimpleIntDataDTO;

import java.io.IOException;

@ContextSingleton
public class JBRssRest {

    private Context context;

    @Inject
    public JBRssRest(Context context) {
        this.context = context;
    }

    @Inject
    private HttpRequestHelper httpRequestHelper;

    @Inject
    private SettingsService settingsService;

    public ListFeedPostDTO getUnreadPosts() throws IOException, UnableToLoginException {
        ListFeedPostDTO ret = null;
        String lastUpdateTime = settingsService.getSetting(Constants.CONFIG_LAST_UPDATE_TIME, "0");
        ret = httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_GET_UNREAD, ListFeedPostDTO.class, new BasicNameValuePair(
                URLConstants.DoJson.Rss.RSS_PARAM_TIMESTAMP, lastUpdateTime));
        if (ret != null)
            settingsService.saveSetting(Constants.CONFIG_LAST_UPDATE_TIME, String.valueOf(ret.timestamp));
        return ret;
    }

    public SimpleIntDataDTO update() throws IOException, UnableToLoginException {
        return httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_UPDATE, SimpleIntDataDTO.class);
    }

    public ListFeedDTO getFeeds() throws IOException, UnableToLoginException {
        return httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_LIST, ListFeedDTO.class);
    }

    public LoginDTO login() throws IOException, UnableToLoginException {
        LoginDTO ret = httpRequestHelper.getForObject(
                URLConstants.DoJson.Login.LOGIN_DO_LOGIN_JSON,
                LoginDTO.class,
                new BasicNameValuePair(URLConstants.DoJson.Login.LOGIN_PARAM_USER, settingsService.getSetting(context.getString(R.string.username),
                        "")),
                new BasicNameValuePair(URLConstants.DoJson.Login.LOGIN_PARAM_PASS, settingsService.getSetting(context.getString(R.string.password),
                        "")));
        if (ret != null && ret.logged)
            settingsService.saveSetting(Constants.CONFIG_SESSION, ret.session);
        return ret;
    }

    public SimpleBooleanDataDTO markReadPost(Integer postId) throws IOException, UnableToLoginException {
        SimpleBooleanDataDTO ret = httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_MARK_READ, SimpleBooleanDataDTO.class,
                new BasicNameValuePair("post", postId.toString()), new BasicNameValuePair("read", "true"));
        Log.i("markReadPost", ret != null ? ret.data.toString() : "ret = null");
        return ret;
    }

    public SimpleBooleanDataDTO markReadFeed(Integer feedId) throws IOException, UnableToLoginException {
        SimpleBooleanDataDTO ret = httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_MARK_READ, SimpleBooleanDataDTO.class,
                new BasicNameValuePair("feed", feedId.toString()), new BasicNameValuePair("read", "true"));
        Log.i("markReadPost", ret != null ? ret.data.toString() : "ret = null");
        return ret;
    }

    public SimpleBooleanDataDTO addFeed(String feedURL) throws IOException, UnableToLoginException {
        return httpRequestHelper.getForObject(URLConstants.DoJson.Rss.RSS_DO_ADD, SimpleBooleanDataDTO.class, new BasicNameValuePair("url", feedURL));
    }

    public CaptchDTO getCaptcha() throws IOException, UnableToLoginException {
        return httpRequestHelper.getForObject(URLConstants.DoJson.Captcha.CAP_GET, CaptchDTO.class);
    }

    public LoginDTO reg(String user, String pass, String cid, String captcha) throws IOException, UnableToLoginException {
        LoginDTO ret = httpRequestHelper.getForObject(URLConstants.DoJson.Login.LOGIN_DO_REGISTER_JSON, LoginDTO.class, new BasicNameValuePair(
                URLConstants.DoJson.Login.LOGIN_PARAM_USER, user), new BasicNameValuePair(URLConstants.DoJson.Login.LOGIN_PARAM_PASS, pass),
                new BasicNameValuePair(URLConstants.DoJson.Login.LOGIN_PARAM_CAPTCHA, cid), new BasicNameValuePair(
                URLConstants.DoJson.Login.LOGIN_PARAM_CAPVAL, captcha));
        return ret;
    }

}
