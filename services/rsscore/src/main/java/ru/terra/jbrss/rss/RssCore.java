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
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;
import ru.terra.jbrss.shared.service.UsersService;
import ru.terra.jbrss.tenancy.TenantDataStoreAccessor;

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
    private UsersService usersService;

    @Autowired
    @Qualifier("tenantFeedsService")
    private FeedService feedService;

    @Autowired
    @Qualifier("tenantFeedPostsService")
    private FeedPostsService feedPostsService;

    @Autowired
    @Qualifier("tenantSettingService")
    private SettingsService settingsService;

    public void updateAll() {
        try {
            usersService.getAllUserIds().forEach(this::scheduleUpdatingForUser);
        } catch (Exception e) {
            logger.error("Unable to schedule update", e);
        }
    }


    public Integer updateFeed(Feeds feed) {
        logger.info("updating feed " + feed.getFeedurl());
        List<Feedposts> posts = downloader.loadFeeds(feed);
        Date d = null;
        List<Feedposts> lastFeedPosts = feedPostsService.getLastPostInFeed(feed.getId());
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

        feedPostsService.save(newPosts);
        return newPosts.size();
    }

    public synchronized void scheduleUpdatingForUser(String uid) {
        TenantDataStoreAccessor.removeConfiguration();
        TenantDataStoreAccessor.setConfiguration(uid);
        logger.info("Scheduling updating for user " + uid);
        try {
            if (sched == null) {
                sched = sf.getScheduler();
                sched.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Settings settings = settingsService.get(SettingsConstants.UPDATE_INTERVAL);
        ScheduleBuilder scheduleBuilder = null;
        Settings updateType = settingsService.get(SettingsConstants.UPDATE_TYPE);
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
                jobDataMap.put("user", uid);
                jobDataMap.put("re", this);
                JobDetail job = JobBuilder.newJob(UpdateJob.class)
                        .withIdentity("user" + uid, "group1")
                        .usingJobData(jobDataMap)
                        .build();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("user" + uid, "group1")
                        .startNow()
                        .withSchedule(scheduleBuilder)
                        .build();
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException se) {
                se.printStackTrace();
                logger.error("Error while initializing quartz", se);
            }
        } else {
            logger.info("Update interval for user " + uid + " is not set, just update one time");
        }
        updateAllFeedsForUser(uid);
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

    public void updateAllFeedsForUser(String uid) {
        TenantDataStoreAccessor.removeConfiguration();
        TenantDataStoreAccessor.setConfiguration(uid);
        List<Feeds> feeds = feedService.getFeeds();
        if (feeds != null && feeds.size() > 0) {
            for (Feeds f : feeds) {
                try {
                    updateFeed(f);
                } catch (Exception e) {
                    logger.error("Error while updating feed " + f.getFeedurl(), e);
                }
            }
        }
    }

    public Downloader getDownloader() {
        return downloader;
    }
}
