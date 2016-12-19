package ru.terra.jbrss.im.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.terra.jbrss.im.core.IMType;
import ru.terra.jbrss.im.core.ServerInterface;

import javax.inject.Inject;

@Component
public class TelegramIM extends ServerInterface {
    @Value("${telegram.botname}")
    protected String telegramBotName;
    @Value("${telegram.token}")
    protected String telegramToken;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private TelegramImBotInterface botInterface;

    @Inject
    public TelegramIM() {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botInterface = new TelegramImBotInterface();
        try {
            botsApi.registerBot(botInterface);
        } catch (TelegramApiException e) {
            logger.error("Unable to start bot interface", e);
        }
    }

    @Override
    public void sendMessage(String contact, String text) {
        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(contact)
                .setText(text);
        try {
            botInterface.sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected IMType getType() {
        return IMType.TELEGRAM;
    }

    private class TelegramImBotInterface extends TelegramLongPollingBot {
        @Override
        public void onUpdateReceived(Update update) {
            // We check if the update has a message and the message has text
            if (update.hasMessage() && update.getMessage().hasText()) {

            }
        }

        @Override
        public String getBotUsername() {
            return telegramBotName;
        }

        @Override
        public String getBotToken() {
            return telegramToken;
        }
    }
}
