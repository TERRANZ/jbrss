package ru.terra.jbrss.config;

import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.terra.jbrss.tenant.TenantDataStoreAccessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        Map<String, Object> pathVars = (Map<String, Object>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVars.containsKey("uid")) {
            TenantDataStoreAccessor.removeConfiguration();
            String uid = pathVars.get("uid").toString();
            LoggerFactory.getLogger(this.getClass()).info("Received uid: " + uid);
            TenantDataStoreAccessor.setConfiguration(uid);
        }
        return true;
    }
}
