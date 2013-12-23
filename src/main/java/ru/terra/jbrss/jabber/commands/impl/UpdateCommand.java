package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 23.12.13
 * Time: 19:33
 */
@JabberCommand(name = "update")
public class UpdateCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        for (Feeds f : rssModel.getFeeds(getUserId()))
            rssModel.updateFeed(f);
        serverInterface.sendMessage(contact, "Update complete");
        return true;
    }
}
