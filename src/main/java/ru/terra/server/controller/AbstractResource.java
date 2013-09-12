package ru.terra.server.controller;


import com.sun.jersey.api.core.HttpContext;
import ru.terra.jbrss.db.entity.User;
import ru.terra.server.security.SecurityLevel;
import ru.terra.server.security.SessionsHolder;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractResource {
    protected SessionsHolder sessionsHolder = SessionsHolder.getInstance();

    protected static String getParameter(HttpContext context, String key) {
        return context.getRequest().getQueryParameters().getFirst(key);
    }

    protected String extractSessionId(HttpContext context) {
        return context.getRequest().getCookieNameValueMap().getFirst("JSESSIONID");
    }

    protected boolean isAuthorized(HttpContext context) {
        String sessionId = extractSessionId(context);
        if (sessionId == null)
            return false;
        return sessionsHolder.getSession(sessionId) != null;
    }

    protected Integer getCurrentUserId(HttpContext context) {
        User u = getCurrentUser(context);
        return u != null ? u.getId() : null;
    }

    protected User getCurrentUser(HttpContext context) {
        try {
            return sessionsHolder.getSession(extractSessionId(context)).getUser();
        } catch (NullPointerException e) {
            return null;
        }
    }

    protected boolean checkUserCanAccess(HttpContext context, SecurityLevel level) {
        User u = getCurrentUser(context);
        if (u != null && u.getLevel() >= level.ordinal())
            return true;
        return false;
    }

}
