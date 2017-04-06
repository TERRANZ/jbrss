package ru.terra.jbrss.im.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.constants.ContactStatus;
import ru.terra.jbrss.db.entity.Contact;
import ru.terra.jbrss.db.repos.ContactsRepository;
import ru.terra.jbrss.shared.dto.FeedDto;
import ru.terra.jbrss.shared.dto.FeedPostDto;
import ru.terra.jbrss.shared.service.RssService;
import ru.terra.jbrss.shared.service.UsersService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public abstract class ServerInterface {
    @Autowired
    protected ContactsRepository contactsRepository;
    @Autowired
    protected UsersService userService;
    @Autowired
    protected RssService rssService;

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
        Integer userId = userService.getUserId(login);
        return c.getUserId().equals(userId);
    }

    public Integer login(String login, String pass) {
        Integer uid = userService.login(login, pass);
        if (uid != null) {
            contactsRepository.findByUserId(uid).forEach(c -> {
                c.setLastlogin(new Date().getTime());
                contactsRepository.save(c);
            });
            return uid;
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
        return rssService.getFeeds(contactsRepository.findByContactAndType(contact, getType().name()).getUserId());
    }

    public List<FeedPostDto> getFeedPosts(Integer userId, Integer targetFeed, Integer page, Integer perPage) {
        return rssService.getFeedPosts(userId, targetFeed, page, perPage);
    }

    public boolean addFeed(String contact, String url) throws IllegalAccessException {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        return rssService.addFeed(c.getUserId(), url);
    }

    public void removeFeed(Integer userId, Integer feedId) {
        rssService.removeFeed(userId, feedId);
    }

    protected void processText(String fromName, String msg) {
        String[] parsedMessage = msg.split(" ");
        AbstractCommand cmd = CommandsFactory.getInstance().getCommand(parsedMessage[0]);
        if (cmd != null) {
            cmd.setServerInterface(this);
            List<String> params = new ArrayList<>(Arrays.asList(parsedMessage));
            params.remove(0);
            if (cmd.needAuth()) {
                cmd.setContact(fromName);
            }
            if (isContactExists(fromName) || !cmd.needAuth())
                try {
                    if (!cmd.doCmd(fromName, params)) {
                        sendMessage(fromName, "Command failed");
                    }
                } catch (Exception e) {
                    logger.error("Error while executing command", e);
                    sendMessage(fromName, "Exception while doing command, " + e.getMessage());
                }
            else {
                sendMessage(fromName, "Hello, you are not registered, type reg");
            }
        }
    }

    public abstract void start();

    public void update(String contact) {
        rssService.updateSchedulingForUser(contactsRepository.findByContactAndType(contact, getType().name()).getUserId());
    }

    public void updateSetting(String key, String val, String contact) {
        rssService.updateSetting(key, val, contactsRepository.findByContactAndType(contact, getType().name()).getUserId());
    }

    public Contact getContact(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name());
    }

    public void updateContact(Contact c) {
        contactsRepository.save(c);
    }

    public void regContact(String contact, Integer answer, String login, String pass) {
        Integer newUserId = userService.createUser(login, pass);
        Contact c = attachContactToUser(contact, newUserId);
        c.setCorrectAnswer(answer.toString());
        c.setStatus(ContactStatus.SENT_QUESTION.ordinal());
        contactsRepository.save(c);
    }

    public void deleteContact(Contact c) {
        c.setStatus(ContactStatus.NOT_READY.ordinal());
        contactsRepository.save(c);
    }

    public Integer getUserId(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name()).getUserId();
    }
}