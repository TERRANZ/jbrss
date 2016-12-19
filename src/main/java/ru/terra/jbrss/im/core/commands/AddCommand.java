package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand("add")
public class AddCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            String url = params.get(0);
            try {
                if (serverInterface.addFeed(contact, url))
                    sendMessage("Feed added completely");
            } catch (IllegalAccessException e) {
                sendMessage("Feed is not added, error = " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }
    }
}
