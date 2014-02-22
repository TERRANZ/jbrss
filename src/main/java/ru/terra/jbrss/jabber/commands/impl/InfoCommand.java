package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

import java.util.List;

/**
 * Date: 20.12.13
 * Time: 16:30
 */
@JabberCommand(name = "info")
public class InfoCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (checkAccess()) {
            List<Feeds> feeds = rssModel.getFeeds(getUserId());
            sendMessage("Your feeds");
            for (Feeds f : feeds)
                sendMessage(f.getId() + " : " + f.getFeedname());
        }
        return false;
    }
}
