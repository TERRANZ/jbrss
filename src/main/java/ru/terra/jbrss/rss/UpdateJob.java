package ru.terra.jbrss.rss;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.model.RssModel;

import java.util.List;

/**
 * Date: 23.05.14
 * Time: 16:06
 */
public class UpdateJob implements Job {
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting update job for user " + jobExecutionContext.getMergedJobDataMap().getIntValue("user"));
        Integer uid = jobExecutionContext.getMergedJobDataMap().getIntValue("user");
        RssModel rssModel = new RssModel();
        List<Feeds> feeds;
        feeds = rssModel.getFeeds(uid);
        if (feeds != null && feeds.size() > 0)
            for (Feeds f : feeds)
                try {
                    rssModel.updateFeed(f);
                } catch (Exception e) {
                    logger.error("Error while updating feed " + f.getFeedurl(), e);
                }

    }
}
