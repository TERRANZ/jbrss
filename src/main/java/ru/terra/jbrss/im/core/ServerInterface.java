package ru.terra.jbrss.im.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.constants.ContactStatus;
import ru.terra.jbrss.core.db.entity.Contact;
import ru.terra.jbrss.core.db.entity.JbrssUser;
import ru.terra.jbrss.core.db.repos.ContactsRepository;
import ru.terra.jbrss.core.db.repos.UsersRepository;
import ru.terra.jbrss.core.rss.RssCore;
import ru.terra.jbrss.web.dto.FeedDto;
import ru.terra.jbrss.web.dto.FeedPostDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class ServerInterface {
    @Autowired
    protected ContactsRepository contactsRepository;
    @Autowired
    protected UsersRepository usersRepository;
    @Autowired
    protected RssCore rssCore;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void sendMessage(String contact, String message);

    protected abstract IMType getType();

    public boolean isContactExists(String contact) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        return c != null && c.getStatus() == ContactStatus.READY.ordinal();
    }

    public boolean isContactAttached(String contact, String login) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        if (c == null)
            return false;
        Integer userId = usersRepository.findByLogin(login).getId();
        return c.getUserId().equals(userId);
    }

    public Integer login(String login, String pass) {
        JbrssUser user = usersRepository.findByLoginAndPassword(login, pass);
        if (user != null) {
            contactsRepository.findByUserId(user.getId()).forEach(c -> {
                c.setLastlogin(new Date().getTime());
                contactsRepository.save(c);
            });
            return user.getId();
        } else
            return null;
    }

    public Contact attachContactToUser(String contact, Integer userId) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        if (c == null)
            c = createContact(contact, userId);
        contactsRepository.save(c);
        return c;
    }

    private Contact createContact(String contact, Integer userId) {
        Contact c = new Contact();
        c.setUserId(userId);
        c.setContact(contact);
        c.setLastlogin(new Date().getTime());
        c.setStatus(ContactStatus.READY.ordinal());
        c.setType(getType().name());
        return c;
    }

    public List<FeedDto> getFeeds(String contact) {
        return rssCore.getFeeds(contactsRepository.findByContactAndType(contact, getType().name()).getUserId()).parallelStream().map(FeedDto::new).collect(Collectors.toList());
    }

    public List<FeedPostDto> getFeedPosts(Integer targetFeed, Integer page, Integer perPage) {
        return rssCore.getFeedPosts(targetFeed, page, perPage).parallelStream().map(FeedPostDto::new).collect(Collectors.toList());
    }

    public boolean addFeed(String contact, String url) throws IllegalAccessException {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        return rssCore.addFeed(usersRepository.findOne(c.getUserId()), url);
    }

    public void removeFeed(Integer feedId) {
        rssCore.removeFeed(feedId);
    }

    protected void processText(String fromName, String msg) {
        String[] parsedMessage = msg.split(" ");
        AbstractCommand cmd = CommandsFactory.getInstance().getCommand(parsedMessage[0]);
        List<String> params = new ArrayList<>(Arrays.asList(parsedMessage));
        params.remove(0);
        if (!isContactExists(fromName))
            sendMessage(fromName, "Hello, you are not registered, type reg");
        else
            if (cmd != null)
            try {
                cmd.setContact(fromName);
                cmd.setServerInterface(this);
                if (!cmd.doCmd(fromName, params)) {
                    sendMessage(fromName, "Command failed");
                }
            } catch (Exception e) {
                logger.error("Error while executing command", e);
                sendMessage(fromName, "Exception while doing command, " + e.getMessage());
            }

    }

    public abstract void start();

    public void update(String contact) {
        rssCore.updateSchedulingForUser(contactsRepository.findByContactAndType(contact, getType().name()).getUserId());
    }

    public void updateSetting(String key, String val, String contact) {
        rssCore.updateSetting(key, val, contactsRepository.findByContactAndType(contact, getType().name()).getUserId());
    }

    public Contact getContact(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name());
    }

    public void updateContact(Contact c) {
        contactsRepository.save(c);
    }

    public void regContact(String contact, Integer answer, String login, String pass) {
        JbrssUser user = new JbrssUser();
        user.setLogin(login);
        user.setPassword(pass);
        usersRepository.save(user);
        Contact c = attachContactToUser(contact, user.getId());
        c.setCorrectAnswer(answer.toString());
        c.setStatus(ContactStatus.SENT_QUESTION.ordinal());
        contactsRepository.save(c);
    }

    public void deleteContact(Contact c) {
        c.setStatus(ContactStatus.NOT_READY.ordinal());
        contactsRepository.save(c);
    }
}