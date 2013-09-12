package ru.terra.jbrss.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.controllers.FeedpostsJpaController;
import ru.terra.jbrss.db.controllers.FeedsJpaController;
import ru.terra.jbrss.db.controllers.UserJpaController;
import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.rss.Downloader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Model {
    private FeedsJpaController feedsJpaController;
    private UserJpaController usersJpaController;
    private FeedpostsJpaController feedpostsJpaController;
    private Downloader downloader = new Downloader();
    private Logger log = LoggerFactory.getLogger(Model.class);

    public Model() {
        feedsJpaController = new FeedsJpaController();
        usersJpaController = new UserJpaController();
        feedpostsJpaController = new FeedpostsJpaController();
    }

    public User getUser(Integer id) {
        try {
            return usersJpaController.get(id);
        } catch (Exception e) {
            log.error("Error while loading user", e);
            return null;
        }
    }

    public User getUser(String user) {
        return usersJpaController.findUser(user);
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
        List<Feedposts> posts = downloader.loadFeeds(feed.getFeedurl());
        Date d = feedpostsJpaController.getLastPostDate(feed.getId());
        List<Feedposts> newPosts;
        if (d != null && posts != null) {
            newPosts = new ArrayList<>();
            for (Feedposts fp : posts) {
                if (fp != null)
                    if (fp.getPostdate() == null)
                        log.info("post date of post " + fp.getPosttitle()+" is null");
                    else if (fp.getPostdate().getTime() > d.getTime())
                        newPosts.add(fp);
            }
        } else {
            newPosts = new ArrayList<>(posts);
        }
        for (Feedposts fp : newPosts) {
            fp.setFeedId(feed.getId());
            feedpostsJpaController.create(fp);
        }
        return newPosts.size();
    }

    public List<Feedposts> getNewUserPosts(Integer uid, Feeds feed, Date d) {
        //log.info("updating feed : " + feed.toString());
        return feedpostsJpaController.findFeedpostsByFeedFromDate(feedsJpaController.findFeedByUserAndById(uid, feed.getId()).getId(), d);

    }

    public List<Feedposts> getFeedPosts(Integer feedId, Integer page, Integer perpage) {
        return feedpostsJpaController.findFeedpostsByFeed(feedId, page, perpage);
    }

    public Boolean isUsersExists(String user) {
        return usersJpaController.findUser(user) != null;
    }

    public Feeds getUserFeed(Integer uid, Integer fid) {
        return feedsJpaController.findFeedByUserAndById(uid, fid);
    }

    public void editFeed(Feeds f) {
        try {
            feedsJpaController.update(f);
        } catch (NonexistentEntityException e) {
            log.error("editFeed", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("editFeed", e);
            e.printStackTrace();
        }
    }

    public void setFeedRead(Integer feed, Boolean read) {
        feedpostsJpaController.setPostsForFeedRead(feed, read);
    }

    public void setPostRead(Integer fp, Boolean read) {
        Feedposts feedpost = null;
        try {
            feedpost = feedpostsJpaController.get(fp);
            if (feedpost != null) {
                feedpost.setRead(read);
                feedpostsJpaController.update(feedpost);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setAllRead(Integer uid) {
    }

    public void removeFeed(Integer id) {
        //To change body of created methods use File | Settings | File Templates.
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
}
