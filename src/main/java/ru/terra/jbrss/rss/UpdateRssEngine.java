package ru.terra.jbrss.rss;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.SettingsConstants;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.engine.SettingsEngine;

/**
 * Date: 23.05.14
 * Time: 16:04
 */
public class UpdateRssEngine {
    private final UpdateRunnable updateRunnable;
    private SettingsEngine settingsEngine;
    private StdSchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler sched;
    private Logger logger = LoggerFactory.getLogger(UpdateRssEngine.class);
    private static UpdateRssEngine instance = new UpdateRssEngine();

    private class UpdateRunnable implements Runnable {

        private UpdateRunnable() {
            try {
                sched = sf.getScheduler();
                sched.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        public synchronized void scheduleUpdatingForUser(Integer userId) {
            Settings settings = settingsEngine.findByKey(SettingsConstants.UPDATE_INTERVAL, userId);
            Integer updateInterval = 0;
            if (settings != null) {
                updateInterval = Integer.parseInt(settings.getValue());
                if (updateInterval > 0)
                    try {
                        JobDetail job = JobBuilder.newJob(UpdateJob.class)
                                .withIdentity("user" + userId.toString(), "group1")
                                .usingJobData("user", userId)
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

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private UpdateRssEngine() {
        settingsEngine = new SettingsEngine();
        updateRunnable = new UpdateRunnable();
        new Thread(updateRunnable).start();
    }

    public void scheduleUpdatingForUser(Integer uid) {
        updateRunnable.scheduleUpdatingForUser(uid);
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

    public static UpdateRssEngine getInstance() {
        return instance;
    }
}
