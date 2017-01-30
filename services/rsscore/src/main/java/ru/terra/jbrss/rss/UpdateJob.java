package ru.terra.jbrss.rss;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.entity.Feeds;

import java.util.List;

@Component
public class UpdateJob implements Job {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting update job for user " + jobExecutionContext.getMergedJobDataMap().getIntValue("user"));
        Integer uid = jobExecutionContext.getMergedJobDataMap().getIntValue("user");
        RssCore rssCore = (RssCore) jobExecutionContext.getMergedJobDataMap().get("re");
        List<Feeds> feeds;
        feeds = rssCore.getFeeds(uid);
        if (feeds != null && feeds.size() > 0)
            for (Feeds f : feeds)
                try {
                    rssCore.updateFeed(f);
                } catch (Exception e) {
                    logger.error("Error while updating feed " + f.getFeedurl(), e);
                }

    }
}