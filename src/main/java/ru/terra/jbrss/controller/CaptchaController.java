package ru.terra.jbrss.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.engine.CaptchaEngine;
import ru.terra.jbrss.util.ResponceUtils;

@Controller
public class CaptchaController
{
	private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

	@Inject
	private CaptchaEngine captchaEngine;

	@RequestMapping(value = URLConstants.DoJson.Captcha.CAP_GET, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<String> get(HttpServletRequest request)
	{
		return ResponceUtils.makeResponce(captchaEngine.getCaptcha());
	}
}
