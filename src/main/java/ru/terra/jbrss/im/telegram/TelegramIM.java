package ru.terra.jbrss.im.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.im.core.AbstractIM;
import ru.terra.jbrss.im.telegram.impl.TelegramInterfaceImpl;

@Component
public class TelegramIM extends AbstractIM {
    @Autowired
    private TelegramInterfaceImpl telegramInterface;
}
