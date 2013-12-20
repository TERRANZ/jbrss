package ru.terra.jbrss.jabber.commands;

import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.jabber.JabberModel;
import ru.terra.jbrss.model.Model;

/**
 * Date: 20.12.13
 * Time: 16:31
 */
public abstract class AbstractPrivCommand extends AbstractCommand {
    protected JabberModel model = new JabberModel();
    protected Model rssModel = new Model();

    protected User getUser() {
        return model.getUser(getContact());
    }

    protected Integer getUserId() {
        return model.getUserId(getContact());
    }
}
