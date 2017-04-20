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
import ru.terra.jbrss.shared.service.RssRequestor;
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
    protected RssRequestor rssRequestor;

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
        return c.getUser().equals(login);
    }

    public String login(String login, String pass) {
        String uid = userService.login(login, pass);
        if (uid != null) {
            contactsRepository.findByUser(uid).forEach(c -> {
                c.setLastlogin(new Date().getTime());
                c.setUser(login);
                c.setPass(pass);
                c.setUid(uid);
                contactsRepository.save(c);
            });
            return uid;
        } else
            return null;
    }

    private Contact createContact(String contact) {
        Contact c = new Contact();
        c.setContact(contact);
        c.setLastlogin(new Date().getTime());
        c.setStatus(ContactStatus.READY.ordinal());
        c.setType(getType().name());
        return c;
    }

    public List<FeedDto> getFeeds(String contact) {
        return rssRequestor.getFeeds(doAuth(contact), uid(contact));
    }

    public List<FeedPostDto> getFeedPosts(String contact, Integer targetFeed, Integer page, Integer perPage) {
        return rssRequestor.getFeedPosts(doAuth(contact), targetFeed, page, perPage, uid(contact));
    }

    public boolean addFeed(String contact, String url) throws IllegalAccessException {
        return rssRequestor.addFeed(doAuth(contact), url, uid(contact));
    }

    public void removeFeed(String contact, Integer feedId) {
        rssRequestor.removeFeed(doAuth(contact), feedId, uid(contact));
    }

    protected void processText(String fromName, String msg) {
        String[] parsedMessage = msg.split(" ");
        AbstractCommand cmd = CommandsFactory.getInstance().getCommand(parsedMessage[0]);
        if (cmd != null) {
            cmd.setServerInterface(this);
            List<String> params = new ArrayList<>(Arrays.asList(parsedMessage));
            params.remove(0);
            cmd.setContact(fromName);
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

    public boolean update(String contact) {
        return rssRequestor.updateSchedulingForUser(doAuth(contact), uid(contact));
    }

    public void updateSetting(String key, String val, String contact) {
        rssRequestor.updateSetting(key, val, doAuth(contact), uid(contact));
    }

    public Contact getContact(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name());
    }

    public void updateContact(Contact c) {
        contactsRepository.save(c);
    }

    public void regContact(String contact, Integer answer, String login, String pass) {
        String uid = userService.createUser(login, pass);
        rssRequestor.createUser(uid);
        Contact c = createContact(contact);
        c.setUser(login);
        c.setPass(pass);
        c.setCorrectAnswer(answer.toString());
        c.setStatus(ContactStatus.SENT_QUESTION.ordinal());
        c.setUid(uid);
        contactsRepository.save(c);
    }

    public void deleteContact(Contact c) {
        c.setStatus(ContactStatus.NOT_READY.ordinal());
        contactsRepository.save(c);
    }

    public String getUserId(String contact) {
        return contactsRepository.findByContactAndType(contact, getType().name()).getAuthToken();
    }

    private String doAuth(String contact) {
        Contact c = contactsRepository.findByContactAndType(contact, getType().name());
        String token = userService.authenticate(c.getUser(), c.getPass());
        if (!token.isEmpty()) {
            c.setAuthToken(token);
            contactsRepository.save(c);
            return token;
        }
        return "";
    }

    private String uid(String contact) {
        return contactsRepository.findByContact(contact).getUid();
    }
}