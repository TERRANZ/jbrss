package ru.terra.jbrss.im.jabber.impl;

import org.springframework.stereotype.Component;
import ru.terra.jbrss.im.core.IMType;
import ru.terra.jbrss.im.core.ServerInterface;

@Component
public class JabberInterfaceImpl extends ServerInterface {
    @Override
    public void sendMessage(String contact, String message) {

    }

    @Override
    protected IMType getType() {
        return IMType.JABBER;
    }
}
