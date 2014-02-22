package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "delete")
public class DelCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (checkAccess()) {
            rssModel.removeFeed(Integer.parseInt(params[1]));
            sendMessage("Feed " + params[1] + " removed");
        }
        return true;
    }
}
