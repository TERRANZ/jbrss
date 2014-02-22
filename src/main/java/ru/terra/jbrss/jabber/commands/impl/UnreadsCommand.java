package ru.terra.jbrss.jabber.commands.impl;

import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

import java.util.Date;
import java.util.List;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "unreads")
public class UnreadsCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (checkAccess()) {
            Integer fid = Integer.parseInt(params[1]);
            Feeds f = rssModel.getFeed(fid);
            if (f != null) {
                List<Feedposts> feedposts = rssModel.getNewUserPosts(getUserId(), f, f.getUpdateTime());
                sendMessage("Since " + f.getUpdateTime().toString() + " you have " + feedposts.size() + " unreaded posts");
                rssModel.setFeedUpdateDate(f, new Date());
                Integer curr = 1;
                for (Feedposts fp : feedposts) {
                    sendMessage("[" + curr + " of " + feedposts.size() + "]" + fp.getPosttitle() + " : " + fp.getPosttext() + " " + fp.getPostlink());
                    curr++;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        LoggerFactory.getLogger(this.getClass()).error("Unable to print feed posts", e);
                    }
                }

            } else {
                sendMessage("This feed number is not exists");
            }
        }
        return true;
    }
}
