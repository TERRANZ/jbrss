package ru.terra.jbrss.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {
    @RequestMapping("/")
    public String main() {
        if (SecurityContextHolder.getContext() != null)
            return "main";
        else
            return "login";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
