package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.constants.SettingsConstants;
import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "set", help = "Set settings command, syntax: set command value")
public class SetCommand extends AbstractCommand {

    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (!serverInterface.isContactExists(contact)) {
            sendMessage("Not authorized");
            return false;
        } else {
            if (params.size() >= 2) {
                switch (params.get(0)) {
                    case "updatetime.minutes": {
                        try {
                            serverInterface.updateSetting(SettingsConstants.UPDATE_TYPE, SettingsConstants.UPDATE_TYPE_SIMPLE, contact);
                            serverInterface.updateSetting(SettingsConstants.UPDATE_INTERVAL, params.get(1), contact);
                            serverInterface.update(contact);
                            sendMessage("New update time set to " + params.get(1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case "updatetime.cron": {
                        try {
                            serverInterface.updateSetting(SettingsConstants.UPDATE_TYPE, SettingsConstants.UPDATE_TYPE_CRON, contact);
                            String cronExpr = "";
                            for (int i = 1; i < params.size(); i++)
                                cronExpr += params.get(i) + " ";
                            serverInterface.updateSetting(SettingsConstants.UPDATE_INTERVAL, cronExpr.trim(), contact);
                            serverInterface.update(contact);
                            sendMessage("New update time set to " + cronExpr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
            return true;
        }
    }
}
