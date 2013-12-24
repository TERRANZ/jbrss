package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.jabber.ServerInterface;
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
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        if (checkAccess()) {
            List<Feeds> feeds = rssModel.getFeeds(getUserId());
            serverInterface.sendMessage(contact, "Your feeds");
            for (Feeds f : feeds) {
                serverInterface.sendMessage(contact, f.getId() + " : " + f.getFeedname());
            }
        }
        return false;
    }
}
