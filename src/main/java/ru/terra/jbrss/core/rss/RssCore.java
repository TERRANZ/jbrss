package ru.terra.jbrss.core.rss;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.constants.SettingsConstants;
import ru.terra.jbrss.core.db.entity.Feedposts;
import ru.terra.jbrss.core.db.entity.Feeds;
import ru.terra.jbrss.core.db.entity.JbrssUser;
import ru.terra.jbrss.core.db.entity.Settings;
import ru.terra.jbrss.core.db.repos.FeedPostsRepository;
import ru.terra.jbrss.core.db.repos.FeedsRepository;
import ru.terra.jbrss.core.db.repos.SettingsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Scope("singleton")
public class RssCore {
    private StdSchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler sched;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Downloader downloader = new Downloader();
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;

    public void runUpdate() {
        usersRepository.findAll().forEach(u -> scheduleUpdatingForUser(u.getId()));
    }

    public List<Feeds> getFeeds(Integer uid) {
        return feedsRepository.findByUserid(uid);
    }

    public Integer updateFeed(Feeds feed) {
        log.info("updating feed " + feed.getFeedurl());
        List<Feedposts> posts = downloader.loadFeeds(feed);
        Date d = feedPostsRepository.getPostsByFeedAndByDateSorted(feed.getId()).get(0).getPostdate();
        List<Feedposts> newPosts;
        if (d != null && posts != null) {
            newPosts = new ArrayList<>();
            for (Feedposts fp : posts) {
                if (fp != null)
                    if (fp.getPostdate() == null)
                        log.info("post date of post " + fp.getPosttitle() + " is null");
                    else if (fp.getPostdate().getTime() > d.getTime()) {
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
        feedPostsRepository.save(newPosts);
        return newPosts.size();
    }

    public synchronized void scheduleUpdatingForUser(Integer userId) {
        try {
            if (sched == null) {
                sched = sf.getScheduler();
                sched.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Settings settings = settingsRepository.findByKeyAndUserId(SettingsConstants.UPDATE_INTERVAL, userId);
        Integer updateInterval;
        if (settings != null) {
            updateInterval = Integer.parseInt(settings.getValue());
            if (updateInterval > 0)
                try {
                    JobDataMap jobDataMap = new JobDataMap();
                    jobDataMap.put("user", userId);
                    jobDataMap.put("re", this);
                    JobDetail job = JobBuilder.newJob(UpdateJob.class)
                            .withIdentity("user" + userId.toString(), "group1")
                            .usingJobData(jobDataMap)
                            .build();
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("user" + userId.toString(), "group1")
                            .startNow()
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule()
                                            .withIntervalInMinutes(updateInterval)
                                            .repeatForever()
                            )
                            .build();
                    sched.scheduleJob(job, trigger);
                } catch (SchedulerException se) {
                    se.printStackTrace();
                    log.error("Error while initializing quartz", se);
                }
        } else {
            log.info("Update interval for user " + userId + " is not set");
        }
    }


    public void updateSchedulingForUser(Integer uid) {
        try {
            sched.deleteJob(new JobKey("user" + uid.toString(), "group1"));
            sched.unscheduleJob(new TriggerKey("user" + uid.toString(), "group1"));
            scheduleUpdatingForUser(uid);
        } catch (SchedulerException e) {
            log.error("Unable to remove job", e);
        }
    }


    public Boolean addFeed(final JbrssUser user, final String url) throws IllegalAccessException {
        if (feedsRepository.findByUseridAndByFeedURL(user.getId(), url) == null) {
            new Thread(() -> feedsRepository.save(new Feeds(0, user.getId(), downloader.getFeedTitle(url), url, new Date()))).start();
            return true;
        }
        return false;
    }

    public List<Feedposts> getNewUserPosts(Feeds feed, Date d) {
        return feedPostsRepository.getPostsByFeedAndByDate(feed.getId(), d);
    }

    public List<Feedposts> getFeedPosts(Integer feedId, Integer page, Integer perpage) {
        return feedPostsRepository.findFeedpostsByFeedLimited(feedId, perpage, page * perpage);
    }

    public void setFeedRead(Integer feed, Boolean read) {
        feedPostsRepository.findByFeedIdAndIsRead(feed, read).parallelStream().forEach(fp -> {
            fp.setRead(false);
            feedPostsRepository.save(fp);
        });
    }

    public void setPostRead(Integer fp, Boolean read) {
        Feedposts feedpost;
        try {
            feedpost = feedPostsRepository.findOne(fp);
            if (feedpost != null) {
                feedpost.setRead(read);
                feedPostsRepository.save(feedpost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllRead(Integer uid) {
        for (Feeds f : feedsRepository.findByUserid(uid))
            setPostRead(f.getId(), true);
    }

    public void removeFeed(Integer id) {
        log.info("Removed posts in feed " + id);
        feedPostsRepository.findByFeedId(id).parallelStream().forEach(fp -> feedPostsRepository.delete(fp));
        try {
            feedsRepository.delete(id);
        } catch (Exception e) {
            log.error("Unable to delete feed " + id, e);
        }
    }

    public Feeds getFeed(Integer id) {
        return feedsRepository.findOne(id);
    }

    public void setFeedUpdateDate(Feeds f, Date date) {
        f.setUpdateTime(date);
        feedsRepository.save(f);
    }

    public List<Feedposts> search(String posttext) {
        return feedPostsRepository.findByPosttextLike(posttext);
    }

    public Feedposts getPost(Integer id) {
        return feedPostsRepository.findOne(id);
    }
}
