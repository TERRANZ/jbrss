package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand("update")
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
