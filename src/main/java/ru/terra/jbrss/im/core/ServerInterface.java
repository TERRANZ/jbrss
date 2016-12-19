package ru.terra.jbrss.im.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.repos.ContactsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;

@Component
public abstract class ServerInterface {
    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private UsersRepository usersRepository;

    public abstract void sendMessage(String contact, String message);

    public boolean isContactExists(String contact) {
        return false;
    }

    public boolean isContactAttached(String contact, String login) {
        return false;
    }

    public Integer login(String login, String pass) {
        return null;
    }

    public void attachContactToUser(String contact, Integer userId) {
    }
}