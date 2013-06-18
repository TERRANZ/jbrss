package ru.terra.jbrss.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.dto.rss.FeedDTO;
import ru.terra.jbrss.web.security.SessionHelper;

@Controller
public class FeedController {

	@RequestMapping(value = URLConstants.Pages.FEED, method = { RequestMethod.GET, RequestMethod.POST })
	private String product(HttpServletRequest request, Locale locale, Model model) {
		if (!SessionHelper.isUserCurrentAuthorized())
			return URLConstants.Views.LOGIN;
		if (request.getParameter(URLConstants.DoJson.Rss.PARAM_FEED_ID) == null)
			return URLConstants.Views.ERROR404;
		Integer feedId = 0;
		try {
			feedId = Integer.parseInt(request.getParameter(URLConstants.DoJson.Rss.PARAM_FEED_ID));
			// Product prod = pe.getProduct(prodId);
			// if (prod == null)
			// return URLConstants.Views.ERROR404;
			// model.addAttribute(ModelConstants.CATEGORY_ID,
			// prod.getCategory().getId());
			// model.addAttribute(URLConstants.DoJson.Products.PRODUCT_PARAM_ID,
			// prodId);
			model.addAttribute("feedid", feedId);
		} catch (NumberFormatException e) {
			return URLConstants.Views.ERROR404;
		}
		return URLConstants.Views.FEED;

	}
}
