package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.terra.jbrss.constants.URLConstants;

@Controller
@RequestMapping(URLConstants.UI.UI)
public class ServiceController {
    @Autowired
    private OAuth2RestOperations restTemplate;
}
