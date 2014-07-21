package ru.terra.jbrss.rss;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Downloader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Downloader() {
    }

    public SyndFeed parseFeed(String url) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

    public String getFeedTitle(String url) {
        try {
            return parseFeed(url).getTitle();
        } catch (IllegalArgumentException e) {
            logger.error("Unable to get feed title", e);
        } catch (MalformedURLException e) {
            logger.error("Unable to get feed title", e);
        } catch (FeedException e) {
            logger.error("Unable to get feed title", e);
        } catch (IOException e) {
            logger.error("Unable to get feed title", e);
        }
        return "unnamed";
    }

    public List<Feedposts> loadFeeds(Feeds feeds) {
        try {
            SyndFeed feed = parseFeed(feeds.getFeedurl());
            List<Feedposts> ret = new ArrayList<Feedposts>();
            for (Object object : feed.getEntries()) {
                SyndEntry entry = (SyndEntry) object;
                Feedposts feedpost = new Feedposts();
                feedpost.setPosttitle(entry.getTitle());
                feedpost.setPostlink(entry.getLink());
                SyndContent content = entry.getDescription();
                if (content != null) {
                    feedpost.setPosttext(content.getValue());
                }
                feedpost.setPostdate(entry.getPublishedDate());
                if (feedpost.getPostdate() == null)
                    feedpost.setPostdate(entry.getUpdatedDate());
                feedpost.setFeedId(feeds.getId());
                feedpost.setUpdated(new Date());
                ret.add(feedpost);
            }
            return ret;
        } catch (IllegalArgumentException e) {
            logger.error("Unable to get load feed", e);
        } catch (MalformedURLException e) {
            logger.error("Unable to get load feed", e);
        } catch (FeedException e) {
            logger.error("Unable to get load feed", e);
        } catch (IOException e) {
            logger.error("Unable to get load feed", e);
        }
        return null;
    }

}
