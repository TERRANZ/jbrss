package ru.terra.jbrss.im.telegram;

import org.springframework.stereotype.Component;
import ru.terra.jbrss.im.core.IMType;
import ru.terra.jbrss.im.core.ServerInterface;

import javax.inject.Inject;

@Component
public class TelegramIM extends ServerInterface {
    @Inject
    public TelegramIM() {
    }

    @Override
    public void sendMessage(String contact, String message) {
    }

    @Override
    protected IMType getType() {
        return IMType.TELEGRAM;
    }
}
