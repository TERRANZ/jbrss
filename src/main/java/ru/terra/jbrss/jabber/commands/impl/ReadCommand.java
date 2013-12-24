package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

import java.util.List;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "read")
public class ReadCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        if (checkAccess()) {
            Integer page = 0;
            Integer perPage = 5;
            Integer targetFeed = Integer.parseInt(params[1]);
            try {
                page = Integer.parseInt(params[2]);
            } catch (Exception e) {
            }
            try {
                perPage = Integer.parseInt(params[3]);
            } catch (Exception e) {
            }
            List<Feedposts> posts = rssModel.getFeedPosts(targetFeed, page, perPage);
            for (Feedposts fp : posts) {
                serverInterface.sendMessage(contact, fp.getPosttitle() + " : " + fp.getPosttext());
            }
        }
        return true;
    }
}
