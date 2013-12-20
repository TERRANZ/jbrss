package ru.terra.jbrss.jabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.dto.captcha.CaptchDTO;
import ru.terra.jbrss.engine.UsersEngine;
import ru.terra.jbrss.engine.YandexCaptcha;
import ru.terra.jbrss.jabber.db.controller.ContactJpaController;
import ru.terra.jbrss.jabber.db.entity.Contact;
import ru.terra.jbrss.jabber.db.entity.ContactStatus;

import java.util.Date;

/**
 * Date: 19.12.13
 * Time: 15:29
 */
public class JabberModel {
    private ContactJpaController contactJpaController = new ContactJpaController(Contact.class);
    private UsersEngine usersEngine = new UsersEngine();
    private YandexCaptcha captchaEngine = new YandexCaptcha();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean isContactExists(String contact) {
        return contactJpaController.isContactExists(contact);
    }

    public String startRegContact(String contact) {
        Contact newContact = new Contact();
        newContact.setStatus(ContactStatus.CAPTCHA_SENT);
        newContact.setContact(contact);
        CaptchDTO dto = captchaEngine.getCaptcha();
        newContact.setCaptchaKey(dto.cid);
        contactJpaController.create(newContact);
        return dto.image;
    }

    public String proceedRegContact(String contact) {
        Contact c = contactJpaController.findByContact(contact);
        if (c == null)
            return "";
        CaptchDTO dto = captchaEngine.getCaptcha();
        c.setCaptchaKey(dto.cid);
        try {
            contactJpaController.update(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto.image;
    }

    public int getContactStatus(String contact) {
        Contact c = contactJpaController.findByContact(contact);
        if (c != null)
            return c.getStatus();
        return -1;
    }

    public boolean completeReg(String contact, String user, String pass, String capval) {
        Contact c = contactJpaController.findByContact(contact);
        if (c != null) {
            if (user != null && usersEngine.findUserByName(user) == null) {
                if (user != null && pass != null) {
                    if (captchaEngine.checkCaptcha(capval, c.getCaptchaKey())) {
                        Integer retId = usersEngine.registerUser(user, pass);
                        c.setUserId(retId);
                        c.setStatus(ContactStatus.USER_REG);
                        c.setCaptchaVal("");
                        c.setCaptchaKey("");
                        try {
                            contactJpaController.update(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        logger.info("Captcha invalid");
                        return false;
                    }
                } else {
                    logger.info("User or pass is null");
                    return false;
                }
            } else {
                logger.info("User already exists");
                return false;
            }

        }
        logger.info("Contact is not exists");
        return false;
    }

    public User getUser(String contact) {
        Contact c = contactJpaController.findByContact(contact);
        if (c == null)
            return null;
        Integer uid = c.getUserId();
        if (uid == null)
            return null;
        try {
            return usersEngine.getUser(uid);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean attachUserToContact(String contact, Integer uid) {
        Contact c = new Contact();
        c.setStatus(ContactStatus.USER_REG);
        c.setContact(contact);
        c.setUserId(uid);
        try {
            contactJpaController.update(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void updateLastLogin(String contact) {
        Contact c = contactJpaController.findByContact(contact);
        if (c != null) {
            c.setLastlogin(new Date().getTime());
        }
    }

    public Integer getUserId(String contact) {
        Contact c = contactJpaController.findByContact(contact);
        if (c != null) {
            return c.getUserId();
        }
        return null;
    }
}
