package ru.terra.jbrss.im.core.commands;


import ru.terra.jbrss.constants.ContactStatus;
import ru.terra.jbrss.db.entity.Contact;
import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "unreg", help = "Unreg comand, syntax: unreg")
public class UnRegCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        Contact c = serverInterface.getContact(contact);
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            c.setStatus(ContactStatus.UNREG.ordinal());
            sendMessage("You are really want to delete registration? [yes/no]");
            serverInterface.updateContact(c);
        }
        return true;
    }
}
