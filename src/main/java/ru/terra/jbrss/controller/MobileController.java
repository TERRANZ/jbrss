package ru.terra.jbrss.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.web.security.SessionHelper;

@Controller
public class MobileController {

	private static final Logger logger = LoggerFactory.getLogger(MobileController.class);

	@RequestMapping(value = URLConstants.Pages.MOBILE, method = { RequestMethod.GET, RequestMethod.POST })
	private String mobile(HttpServletRequest request, Locale locale, Model model) {
		logger.info("loading mobile page");
		if (!SessionHelper.isUserCurrentAuthorized()) {
			logger.info("redirecting to login");
			return "redirect:/" + URLConstants.Pages.MOBILE_LOGIN;
		}
		return URLConstants.Views.MOBILE;
	}

	@RequestMapping(value = URLConstants.Pages.MOBILE_LOGIN, method = { RequestMethod.GET, RequestMethod.POST })
	private String mobileLogin(HttpServletRequest request, Locale locale, Model model) {
		return URLConstants.Views.MOBILE_LOGIN;
	}
}
