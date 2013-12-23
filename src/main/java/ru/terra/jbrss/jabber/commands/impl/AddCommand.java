package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "add")
public class AddCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        String url = params[1];
        try {
            if (rssModel.addFeed(getUser(), url))
                serverInterface.sendMessage(contact, "Feed added completely");
        } catch (IllegalAccessException e) {
            serverInterface.sendMessage(contact, "Feed is not added, error = " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}
