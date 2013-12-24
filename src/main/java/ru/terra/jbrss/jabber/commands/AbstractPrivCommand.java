package ru.terra.jbrss.jabber.commands;

import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.jabber.JabberModel;
import ru.terra.jbrss.model.RssModel;

/**
 * Date: 20.12.13
 * Time: 16:31
 */
public abstract class AbstractPrivCommand extends AbstractCommand {
    protected JabberModel jabberModel = new JabberModel();
    protected RssModel rssModel = new RssModel();

    protected User getUser() {
        return jabberModel.getUser(getContact());
    }

    protected Integer getUserId() {
        return jabberModel.getUserId(getContact());
    }

    protected boolean checkAccess() {
        return getContact() != null && getUser() != null;
    }

}
