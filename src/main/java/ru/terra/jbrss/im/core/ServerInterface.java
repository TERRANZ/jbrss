package ru.terra.jbrss.im.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.entity.Contact;
import ru.terra.jbrss.core.db.entity.JbrssUser;
import ru.terra.jbrss.core.db.repos.ContactsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;
import ru.terra.jbrss.core.rss.RssCore;
import ru.terra.jbrss.web.dto.FeedDto;
import ru.terra.jbrss.web.dto.FeedPostDto;

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

    protected abstract IMType getType();

    public boolean isContactExists(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name()) != null;
    }

    public boolean isContactAttached(String contact, String login) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        Integer userId = usersRepository.findByLogin(login).getId();
        return c.getUserId().equals(userId);
    }

    public Integer login(String login, String pass) {
        JbrssUser user = usersRepository.findByLoginAndPassword(login, pass);
        return user != null ? user.getId() : null;
    }

    public void attachContactToUser(String contact, Integer userId) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        c.setUserId(userId);
        contactsRepository.save(c);
    }

    public List<FeedDto> getFeeds(String contact) {
        return rssCore.getFeeds(contactsRepository.findByContactAndType(contact, getType().name()).getId()).parallelStream().map(FeedDto::new).collect(Collectors.toList());
    }

    public List<FeedPostDto> getFeedPosts(Integer targetFeed, Integer page, Integer perPage) {
        return rssCore.getFeedPosts(targetFeed, page, perPage).parallelStream().map(FeedPostDto::new).collect(Collectors.toList());
    }

    public boolean addFeed(String contact, String url) throws IllegalAccessException {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        return rssCore.addFeed(usersRepository.findOne(c.getUserId()),url);
    }
}