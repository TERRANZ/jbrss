package ru.terra.jbrss.core.rss;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.constants.SettingsConstants;
import ru.terra.jbrss.core.db.entity.Feeds;
import ru.terra.jbrss.core.db.entity.Settings;
import ru.terra.jbrss.core.db.repos.SettingsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;

import java.util.List;

@Service
@Scope("singleton")
public class RssCore {
    private StdSchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler sched;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private UsersRepository usersRepository;

    public void runUpdate() {
        usersRepository.findAll().forEach(u -> scheduleUpdatingForUser(u.getId()));
    }

    public List<Feeds> getFeeds(Integer uid) {
        return null;
    }

    public void updateFeed(Feeds f) {

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
                    logger.error("Error while initializing quartz", se);
                }
        } else {
            logger.info("Update interval for user " + userId + " is not set");
        }
    }


    public void updateSchedulingForUser(Integer uid) {
        try {
            sched.deleteJob(new JobKey("user" + uid.toString(), "group1"));
            sched.unscheduleJob(new TriggerKey("user" + uid.toString(), "group1"));
            scheduleUpdatingForUser(uid);
        } catch (SchedulerException e) {
            logger.error("Unable to remove job", e);
        }
    }
}
