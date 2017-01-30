package ru.terra.jbrss.core.commands;

import ru.terra.jbrss.core.AbstractCommand;
import ru.terra.jbrss.core.IMCommand;

import java.util.List;

@IMCommand(value = "add", help = "Add new rss url, syntax: add url")
public class AddCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            String url = params.get(0);
            try {
                if (serverInterface.addFeed(contact, url)) {
                    sendMessage("Feed added completely");
                    return true;
                } else
                    return false;
            } catch (IllegalAccessException e) {
                sendMessage("Feed is not added, error = " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }
}
