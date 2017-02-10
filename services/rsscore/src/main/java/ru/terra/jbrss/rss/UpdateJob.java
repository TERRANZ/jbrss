package ru.terra.jbrss.rss;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateJob implements Job {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting update job for user " + jobExecutionContext.getMergedJobDataMap().getIntValue("user"));
        Integer uid = jobExecutionContext.getMergedJobDataMap().getIntValue("user");
        RssCore rssCore = (RssCore) jobExecutionContext.getMergedJobDataMap().get("re");
        rssCore.updateAllFeedsForUser(uid);
    }
}