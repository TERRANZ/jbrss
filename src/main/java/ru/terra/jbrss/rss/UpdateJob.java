package ru.terra.jbrss.rss;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Date: 23.05.14
 * Time: 16:06
 */
public class UpdateJob implements Job {
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting update job");
    }
}
