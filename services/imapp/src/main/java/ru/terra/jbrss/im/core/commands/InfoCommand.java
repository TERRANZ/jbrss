package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "info", help = "Informational command, show list of feeds")
public class InfoCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (serverInterface.isContactExists(contact)) {
            sendMessage("Your feeds");
            serverInterface.getFeeds(contact).forEach(f -> sendMessage(f.getId() + " : " + f.getFeedname()));
        } else
            sendMessage("You are not authorized, type login to authorize or help to read other commands");
        return true;
    }
}