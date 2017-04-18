package ru.terra.jbrss.rss;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.constants.SettingsConstants;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.db.entity.base.BaseFeeds;
import ru.terra.jbrss.db.entity.nontenant.NonTenantFeeds;
import ru.terra.jbrss.db.entity.tenant.TenantFeeds;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.SettingsRepository;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.shared.service.UsersService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Scope("singleton")
public class RssCore {
    private StdSchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler sched;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Downloader downloader = new Downloader();

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    @Qualifier("nonTenantFeedsService")
    private FeedService<NonTenantFeeds> nonTenantfeedService;
    @Autowired
    @Qualifier("tenantFeedsService")
    private FeedService<TenantFeeds> tenantFeedService;

    public void start() {
//        usersService.getAllUserIds().forEach(this::scheduleUpdatingForUser);
    }

    public List<NonTenantFeeds> getFeeds() {
        return nonTenantfeedService.getFeeds();
    }

    public Integer updateFeed(BaseFeeds feed) {
        logger.info("updating feed " + feed.getFeedurl());
        List<Feedposts> posts = downloader.loadFeeds(feed);
        Date d = null;
        List<Feedposts> lastFeedPosts = feedPostsRepository.getLastPostInFeed(feed.getId());
        if (!lastFeedPosts.isEmpty())
            d = lastFeedPosts.get(0).getPostdate();

        List<Feedposts> newPosts;
        if (d != null && posts != null) {
            newPosts = new ArrayList<>();
            for (Feedposts fp : posts) {
                if (fp != null)
                    if (fp.getPostdate() == null)
                        logger.info("post date of post " + fp.getPosttitle() + " is null");
                    else if (fp.getPostdate().getTime() > d.getTime()) {
                        fp.setFeedId(feed.getId());
                        fp.setUpdated(new Date());
                        newPosts.add(fp);
                    }
            }
        } else {
            if (posts != null) {
                newPosts = new ArrayList<>(posts);
            } else
                newPosts = new ArrayList<>();
        }
//        if (newPosts.size() > 0)
//            imManager.notifyFeedUpdated(feed.getUserid(), feed, newPosts);
        feedPostsRepository.save(newPosts);
        return newPosts.size();
    }

    public synchronized void scheduleUpdatingForUser(String userId) {
        logger.info("Scheduling updating for user " + userId);
        try {
            if (sched == null) {
                sched = sf.getScheduler();
                sched.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Settings settings = settingsRepository.findByKeyAndUserId(SettingsConstants.UPDATE_INTERVAL, userId);
        ScheduleBuilder scheduleBuilder = null;
        Settings updateType = settingsRepository.findByKeyAndUserId(SettingsConstants.UPDATE_TYPE, userId);
        String updateInterval;
        if (settings != null) {
            updateInterval = settings.getValue();
            if (updateType == null) {
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(Integer.parseInt(updateInterval))
                        .repeatForever();
            } else {
                if (updateType.getValue().equals(SettingsConstants.UPDATE_TYPE_CRON)) {
                    scheduleBuilder = CronScheduleBuilder.cronSchedule(updateInterval);
                } else {
                    scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMinutes(Integer.parseInt(updateInterval))
                            .repeatForever();
                }
            }
            try {
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("user", userId);
                jobDataMap.put("re", this);
                JobDetail job = JobBuilder.newJob(UpdateJob.class)
                        .withIdentity("user" + userId, "group1")
                        .usingJobData(jobDataMap)
                        .build();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("user" + userId, "group1")
                        .startNow()
                        .withSchedule(scheduleBuilder)
                        .build();
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException se) {
                se.printStackTrace();
                logger.error("Error while initializing quartz", se);
            }
        } else {
            logger.info("Update interval for user " + userId + " is not set, just update one time");
        }
        updateAllFeedsForUser(userId);
    }


    public boolean updateSchedulingForUser(String uid) {
        try {
            JobKey jk = new JobKey("user" + uid, "group1");
            if (sched != null && sched.checkExists(jk)) {
                sched.deleteJob(jk);
                sched.unscheduleJob(new TriggerKey("user" + uid, "group1"));
            }
            scheduleUpdatingForUser(uid);
        } catch (SchedulerException e) {
            logger.error("Unable to remove job", e);
            return false;
        }
        return true;
    }


    public Boolean addFeed(final String userId, final String url) throws IllegalAccessException {
//        if (nonTenantFeedsRepository.findByUseridAndByFeedURL(userId, url).isEmpty()) {
//            nonTenantFeedsRepository.save(new BaseFeeds(0, downloader.getFeedTitle(url), url, new Date()));
//            return true;
//        }
        return false;
    }

    public List<Feedposts> getNewUserPosts(BaseFeeds feed, Date d) {
        return feedPostsRepository.getPostsByFeedAfterDate(feed.getId(), d);
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

    public void setAllRead(String uid) {
//        for (BaseFeeds f : nonTenantFeedsRepository.findByUserid(uid))
//            setPostRead(f.getId(), true);
    }

    public boolean removeFeed(Integer id) {
        logger.info("Removed posts in feed " + id);
        feedPostsRepository.findByFeedId(id).parallelStream().forEach(fp -> feedPostsRepository.delete(fp));
//        try {
//            nonTenantFeedsRepository.delete(id);
//        } catch (Exception e) {
//            logger.error("Unable to delete feed " + id, e);
//            return false;
//        }
        return true;
    }

    public List<Feedposts> search(String posttext) {
        return feedPostsRepository.findByPosttextLike(posttext);
    }

    public Feedposts getPost(Integer id) {
        return feedPostsRepository.findOne(id);
    }

    public void updateSetting(String key, String val, String userId) {
        Settings settings = settingsRepository.findByKeyAndUserId(key, userId);
        if (settings != null)
            settings.setValue(val);
        else {
            settings = new Settings();
            settings.setKey(key);
            settings.setValue(val);
            settings.setUserId(userId);
        }
        settingsRepository.save(settings);
    }

    public void updateAllFeedsForUser(String uid) {
        List<NonTenantFeeds> feeds = getFeeds();
        if (feeds != null && feeds.size() > 0) {
            for (BaseFeeds f : feeds) {
                try {
                    updateFeed(f);
                } catch (Exception e) {
                    logger.error("Error while updating feed " + f.getFeedurl(), e);
                }
            }
        }
    }
}
