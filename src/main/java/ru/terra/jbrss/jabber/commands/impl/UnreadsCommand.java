package ru.terra.jbrss.jabber.commands.impl;

import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

import java.util.Date;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "unreads")
public class UnreadsCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        if (checkAccess()) {
            Integer fid = Integer.parseInt(params[1]);
            Feeds f = rssModel.getFeed(fid);
            if (f != null) {
                for (Feedposts fp : rssModel.getNewUserPosts(getUserId(), f, f.getUpdateTime())) {
                    serverInterface.sendMessage(contact, fp.getPosttitle() + " : " + fp.getPosttext() + " " + fp.getPostlink());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        LoggerFactory.getLogger(this.getClass()).error("Unable to print feed posts", e);
                    }
                }
                rssModel.setFeedUpdateDate(f, new Date());
            } else {
                serverInterface.sendMessage(contact, "This feed number is not exists");
            }
        }
        return true;
    }
}
