package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "add")
public class AddCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (checkAccess()) {
            String url = params[1];
            try {
                if (rssModel.addFeed(getUser(), url))
                    sendMessage("Feed added completely");
            } catch (IllegalAccessException e) {
                sendMessage("Feed is not added, error = " + e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }
}
