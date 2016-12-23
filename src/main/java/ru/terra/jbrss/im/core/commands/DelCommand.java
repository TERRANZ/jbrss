package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "del", help = "Delete feed command, syntax: del id . To get ids send info command")
public class DelCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            serverInterface.removeFeed(Integer.parseInt(params.get(0)));
            sendMessage("Feed " + params.get(0) + " removed");
            return true;
        }
    }
}
