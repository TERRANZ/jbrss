package ru.terra.jbrss.rss;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Downloader {

    public Downloader() {
    }

    public SyndFeed parseFeed(String url) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

    public void printRSSContent(SyndFeed feed) {
        System.out.println("About feed:");
        System.out.println("Author: " + feed.getAuthor());
        System.out.println("Authors:");
        if (feed.getAuthors() != null) {
            for (Object author : feed.getAuthors()) {
                System.out.println(((SyndPerson) author).getName());
                System.out.println(((SyndPerson) author).getEmail());
                System.out.println(((SyndPerson) author).getUri());
                System.out.println();
            }
        }
        System.out.println("Title: " + feed.getTitle());
        System.out.println("Title Ex: " + feed.getTitleEx());
        System.out.println("Description: " + feed.getDescription());
        System.out.println("Description Ex: " + feed.getDescriptionEx());
        System.out.println("Date " + feed.getPublishedDate());
        System.out.println("Type: " + feed.getFeedType());
        System.out.println("Encoding: " + feed.getEncoding());
        System.out.println("(C) " + feed.getCopyright());
        System.out.println();
        for (Object object : feed.getEntries()) {
            SyndEntry entry = (SyndEntry) object;
            System.out.println(entry.getTitle() + " - " + entry.getAuthor());
            System.out.println(entry.getLink());
            for (Object contobj : entry.getContents()) {
                SyndContent content = (SyndContent) contobj;
                System.out.println(content.getType());
                System.out.println(content.getValue());
            }
            SyndContent content = entry.getDescription();
            if (content != null)
                System.out.println(content.getValue());
            System.out.println(entry.getPublishedDate());
            System.out.println();
        }
    }

    public String getFeedTitle(String url) {
        try {
            return parseFeed(url).getTitle();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                feedpost.setFeedId(feeds.getId());
                feedpost.setUpdated(new Date());
                ret.add(feedpost);
            }
            return ret;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
