package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

@IMCommand("read")
public class ReadCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
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
            serverInterface.getFeedPosts(targetFeed, page, perPage).forEach(fp -> sendMessage(fp.getPosttitle() + " : " + fp.getPosttext()));
            return true;
        }
    }
}
