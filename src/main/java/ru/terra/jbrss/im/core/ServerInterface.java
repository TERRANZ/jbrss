package ru.terra.jbrss.im.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.repos.ContactsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;
import ru.terra.jbrss.core.rss.RssCore;
import ru.terra.jbrss.web.dto.FeedDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class ServerInterface {
    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RssCore rssCore;

    public abstract void sendMessage(String contact, String message);

    public boolean isContactExists(String contact) {
        return contactsRepository.findByContact(contact) != null;
    }

    public boolean isContactAttached(String contact, String login) {
        return false;
    }

    public Integer login(String login, String pass) {
        return null;
    }

    public void attachContactToUser(String contact, Integer userId) {
    }

    public List<FeedDto> getFeeds(String contact) {
        return rssCore.getFeeds(contactsRepository.findByContact(contact).getId()).parallelStream().map(FeedDto::new).collect(Collectors.toList());
    }
}