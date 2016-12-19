package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

@IMCommand("del")
public class DelCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            serverInterface.removeFeed(Integer.parseInt(params[1]));
            sendMessage("Feed " + params[1] + " removed");
            return true;
        }
    }
}
