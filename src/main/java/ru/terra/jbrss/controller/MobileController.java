package ru.terra.jbrss.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.web.security.SessionHelper;

@Controller
public class MobileController {

	@RequestMapping(value = URLConstants.Pages.MOBILE, method = { RequestMethod.GET, RequestMethod.POST })
	private String mobile(HttpServletRequest request, Locale locale, Model model) {
		if (!SessionHelper.isUserCurrentAuthorized())
			return URLConstants.Views.MOBILE_LOGIN;
		return URLConstants.Views.MOBILE;
	}

	@RequestMapping(value = URLConstants.Pages.MOBILE_LOGIN, method = { RequestMethod.GET, RequestMethod.POST })
	private String mobileLogin(HttpServletRequest request, Locale locale, Model model) {
		return URLConstants.Views.MOBILE_LOGIN;
	}
}
