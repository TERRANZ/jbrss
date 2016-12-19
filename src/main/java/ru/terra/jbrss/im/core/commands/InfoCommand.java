package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

@IMCommand("info")
public class InfoCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (serverInterface.isContactExists(contact)) {
            sendMessage("Your feeds");
            serverInterface.getFeeds(contact).forEach(f -> sendMessage(f.getId() + " : " + f.getFeedname()));
        } else
            sendMessage("You are not authorized, type login to authorize or help to read other commands");
        return true;
    }
}
