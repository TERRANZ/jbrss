package ru.terra.jbrss.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.controllers.FeedpostsJpaController;
import ru.terra.jbrss.db.controllers.FeedsJpaController;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.rss.Downloader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RssModel {
    private FeedsJpaController feedsJpaController;
    private FeedpostsJpaController feedpostsJpaController;
    private Downloader downloader = new Downloader();
    private Logger log = LoggerFactory.getLogger(RssModel.class);

    public RssModel() {
        feedsJpaController = new FeedsJpaController();
        feedpostsJpaController = new FeedpostsJpaController();
    }


    public Boolean addFeed(final User user, final String url) throws IllegalAccessException {
        if (feedsJpaController.findFeedByUserAndByURL(user.getId(), url) == null) {
            new Thread(new Runnable() {
                public void run() {
                    Feeds f = new Feeds(0, user.getId(), downloader.getFeedTitle(url), url, new Date());
                    feedsJpaController.create(f);
                }
            }).start();
            return true;
        }
        return false;
    }

    public List<Feeds> getFeeds(Integer uid) {
        return feedsJpaController.findFeedsByUser(uid);
    }

    public Integer updateFeed(Feeds feed) {
        //log.info("updating feed " + feed.getFeedurl());
        List<Feedposts> posts = downloader.loadFeeds(feed);
        Date d = feedpostsJpaController.getLastPostDate(feed.getId());
        List<Feedposts> newPosts;
        if (d != null && posts != null) {
            newPosts = new ArrayList<>();
            for (Feedposts fp : posts) {
                if (fp != null)
                    if (fp.getPostdate() == null)
                        log.info("post date of post " + fp.getPosttitle() + " is null");
                    else if (fp.getPostdate().getTime() > d.getTime()) {
                        log.info("setting feed id " + feed.getId());
                        fp.setFeedId(feed.getId());
                        fp.setUpdated(new Date());
                        newPosts.add(fp);
                    }
            }
        } else {
            if (posts != null)
                newPosts = new ArrayList<>(posts);
            else
                newPosts = new ArrayList<>();
        }
        feedpostsJpaController.create(newPosts);
        return newPosts.size();
    }

    public List<Feedposts> getNewUserPosts(Integer uid, Feeds feed, Date d) {
        //log.info("updating feed : " + feed.toString());
        return feedpostsJpaController.findFeedpostsByFeedFromDate(feedsJpaController.findFeedByUserAndById(uid, feed.getId()).getId(), d);

    }

    public List<Feedposts> getFeedPosts(Integer feedId, Integer page, Integer perpage) {
        return feedpostsJpaController.findFeedpostsByFeed(feedId, page, perpage);
    }

    public void setFeedRead(Integer feed, Boolean read) {
        feedpostsJpaController.setPostsForFeedRead(feed, read);
    }

    public void setPostRead(Integer fp, Boolean read) {
        Feedposts feedpost;
        try {
            feedpost = feedpostsJpaController.get(fp);
            if (feedpost != null) {
                feedpost.setRead(read);
                feedpostsJpaController.update(feedpost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllRead(Integer uid) {
        for (Feeds f : feedsJpaController.findFeedsByUser(uid))
            feedpostsJpaController.setPostsForFeedRead(f.getId(), true);
    }

    public void removeFeed(Integer id) {
        log.info("Removed posts in feed " + id + " : " + feedpostsJpaController.removePosts(id));
        try {
            feedsJpaController.delete(id);
        } catch (Exception e) {
            log.error("Unable to delete feed " + id, e);
        }
    }

//    private Feedposts process(Feeds feed, TMessage msg) {
//        Feedposts fp = new Feedposts();
//        fp.setFeedId(feed.getId());
//        fp.setId(msg.getNum().intValue());
//        fp.setPostdate(new Date(msg.getMsgtimestamp()));
//        fp.setPostlink(feed.getFeedurl());
//        fp.setPosttext(msg.getComment());
//        fp.setPosttitle(msg.getSubject());
//        fp.setRead(false);
//        fp.setUpdated(new Date());
//        return fp;
//    }

    public Feeds getFeed(Integer id) {
        try {
            return feedsJpaController.get(id);
        } catch (Exception e) {
            log.error("Unable to get feed", e);
        }
        return null;
    }

    public void setFeedUpdateDate(Feeds f, Date date) {
        f.setUpdateTime(date);
        try {
            feedsJpaController.update(f);
        } catch (Exception e) {
            log.error("Unable to update time on feed", e);
        }
    }
}
