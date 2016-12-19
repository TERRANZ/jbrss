package ru.terra.jbrss.im.jabber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.im.core.AbstractIM;
import ru.terra.jbrss.im.jabber.impl.JabberInterfaceImpl;

@Component
public class JabberIM extends AbstractIM {
    @Autowired
    private JabberInterfaceImpl jabberInterface;
}
