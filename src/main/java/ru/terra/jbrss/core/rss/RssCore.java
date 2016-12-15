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
        return feedsRepository.findByUserId(uid);
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
}
