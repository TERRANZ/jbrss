package ru.terra.jbrss.core.commands;

import ru.terra.jbrss.constants.ContactStatus;
import ru.terra.jbrss.core.db.entity.Contact;
import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "default", help = "Default command without params")
public class DefaultCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        Contact c = serverInterface.getContact(contact);
        if (c != null) {
            if (params.size() > 0)
                if (c.getStatus() == ContactStatus.SENT_QUESTION.ordinal()) {
                    if (params.get(0).equals(c.getCorrectAnswer())) {
                        sendMessage("Answer is correct, your registration is complete, send help for instructions");
                        c.setStatus(ContactStatus.READY.ordinal());
                        serverInterface.updateContact(c);
                    } else {
                        sendMessage("Answer is not correct");
                    }
                } else if (c.getStatus() == ContactStatus.UNREG.ordinal()) {
                    if (params.get(0).equalsIgnoreCase("yes")) {
                        serverInterface.deleteContact(c);
                    }
                }

        } else
            sendMessage("Hello, this is default command output, send help for more information");
        return true;
    }
}
