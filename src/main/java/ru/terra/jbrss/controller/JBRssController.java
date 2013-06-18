package ru.terra.jbrss.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.terra.jbrss.constants.URLConstants;

@Controller
public class JBRssController {
	@RequestMapping(value = URLConstants.Pages.JBRSS_HOME, method = RequestMethod.GET)
	public String home(Locale locale, Model model)
	{
		return URLConstants.Views.JBRSS_HOME;
	}
	@RequestMapping(value = URLConstants.Pages.ABOUT, method = RequestMethod.GET)
	public String about(Locale locale, Model model)
	{
		return URLConstants.Views.ABOUT;
	}
	@RequestMapping(value = URLConstants.Pages.CONTACTS, method = RequestMethod.GET)
	public String contacts(Locale locale, Model model)
	{
		return URLConstants.Views.CONTACTS;
	}
}
