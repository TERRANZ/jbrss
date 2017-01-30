package ru.terra.jbrss.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "update", help = "Update all feed command, no params, after updates you will receive new feed posts")
public class UpdateCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            serverInterface.update(contact);
            return true;
        }
    }
}
