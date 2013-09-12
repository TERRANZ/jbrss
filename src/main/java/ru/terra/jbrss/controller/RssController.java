package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.dto.rss.FeedDTO;
import ru.terra.jbrss.dto.rss.FeedPostDTO;
import ru.terra.jbrss.model.Model;
import ru.terra.server.constants.ErrorConstants;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.ListDTO;
import ru.terra.server.dto.SimpleDataDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path(URLConstants.DoJson.Rss.RSS)
public class RssController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Model model = new Model();

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_LIST)
    public ListDTO<FeedDTO> getList(@Context HttpContext hc) {
        logger.info("Loading list of feeds");
        User user = getCurrentUser(hc);
        ListDTO<FeedDTO> ret = new ListDTO<>();
        if (user != null) {
            List<Feeds> feeds;
            feeds = model.getFeeds(user.getId());
            if (feeds != null && feeds.size() > 0) {
                List<FeedDTO> data = new ArrayList<>();
                for (Feeds f : feeds)
                    data.add(new FeedDTO(f));
                ret.setData(data);
            }
            return ret;
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_ADD)
    public SimpleDataDTO<Boolean> add(@Context HttpContext hc, @QueryParam("url") String url) {
        logger.info("Adding new feed");
        User user = getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(false);
        if (user != null) {
            try {
                model.addFeed(user, url);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ret.errorMessage = e.getLocalizedMessage();
                ret.errorCode = ErrorConstants.ERR_UNABLE_TO_ADD_RSS;
                return ret;
            }
            return new SimpleDataDTO<>(true);
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_GET_UNREAD)
    public ListDTO<FeedPostDTO> getUnread(@Context HttpContext hc, @QueryParam("timestamp") long timestamp) {
        ListDTO<FeedPostDTO> ret = new ListDTO<>();
        logger.info("Getting unread feeds");
        User user = getCurrentUser(hc);
        if (user != null) {
            List<Feeds> feeds;
            feeds = model.getFeeds(user.getId());
            if (feeds != null && feeds.size() > 0) {
                List<Feedposts> posts = new ArrayList<>();
                List<FeedPostDTO> dtos = new ArrayList<>();
                for (Feeds feed : feeds) {
                    posts.addAll(model.getNewUserPosts(user.getId(), feed, new Date(timestamp)));
                }
                if (posts != null && posts.size() > 0) {
                    for (Feedposts fp : posts)
                        dtos.add(new FeedPostDTO(fp));
                    ret.setData(dtos);
                }
            }
            return ret;
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_MARK_READ)
    public SimpleDataDTO<Boolean> markRead(@Context HttpContext hc, @QueryParam("read") Boolean read) {
        Integer feed = null;
        if (getParameter(hc, "feed") != null)
            feed = Integer.parseInt(getParameter(hc, "feed"));
        Integer post = null;
        if (getParameter(hc, "post") != null)
            post = Integer.parseInt(getParameter(hc, "post"));
        Boolean all = null;
        if (getParameter(hc, "all") != null)
            all = Boolean.getBoolean(getParameter(hc, "all"));
        logger.info("Marking read");
        User user = getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(true);
        if (user != null) {
            if (feed != null) {
                model.setFeedRead(feed, read);
                return ret;
            } else if (post != null) {
                model.setPostRead(post, read);
                return ret;
            } else if (all) {
                model.setAllRead(user.getId());
                return ret;
            } else {
                ret.data = false;
                return ret;
            }
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_UPDATE)
    public SimpleDataDTO<Integer> update(@Context HttpContext hc) {
        logger.info("Updating feeds");
        User user = getCurrentUser(hc);
        SimpleDataDTO<Integer> ret = new SimpleDataDTO<>(0);
        if (user != null) {
            List<Feeds> feeds;
            feeds = model.getFeeds(user.getId());
            Integer updated = 0;
            if (feeds != null && feeds.size() > 0) {
                for (Feeds f : feeds)
                    try {
                        updated += model.updateFeed(f);
                    } catch (Exception e) {
                        logger.error("Error while updating feed " + f.getFeedurl(), e);
                    }
            }
            ret.data = updated;
            return ret;
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_DEL)
    public SimpleDataDTO<Boolean> delete(@Context HttpContext hc, @QueryParam("id") Integer id) {
        logger.info("Delete feed");
        User user = getCurrentUser(hc);
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>(true);
        if (user != null) {
            model.removeFeed(id);
            return ret;
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }

    @GET
    @Path(URLConstants.DoJson.Rss.RSS_DO_GET_FEED)
    public ListDTO<FeedPostDTO> getFeed(@Context HttpContext hc, @QueryParam("id") Integer id, @QueryParam("count") Integer count) {
        ListDTO<FeedPostDTO> ret = new ListDTO<>();
        logger.info("Getting feed");
        User user = getCurrentUser(hc);
        if (user != null) {
            List<Feedposts> posts = new ArrayList<>();
            List<FeedPostDTO> dtos = new ArrayList<>();
            posts.addAll(model.getFeedPosts(id, 0, count));
            if (posts != null && posts.size() > 0) {
                for (Feedposts fp : posts)
                    dtos.add(new FeedPostDTO(fp));
                ret.setData(dtos);
            }
            return ret;
        } else {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
    }
}
