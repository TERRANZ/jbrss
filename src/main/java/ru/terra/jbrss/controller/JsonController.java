package ru.terra.jbrss.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.terra.jbrss.constants.ErrorConstants;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.dto.ListDTO;
import ru.terra.jbrss.dto.SimpleDataDTO;
import ru.terra.jbrss.dto.rss.FeedDTO;
import ru.terra.jbrss.dto.rss.FeedPostDTO;
import ru.terra.jbrss.model.Model;
import ru.terra.jbrss.util.ResponceUtils;
import ru.terra.jbrss.web.security.SessionHelper;

@Controller
public class JsonController {
	private static final Logger logger = LoggerFactory.getLogger(JsonController.class);

	@Inject
	private Model model;

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_LIST, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoList(HttpServletRequest request) {
		logger.info("Loading list of feeds");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			List<Feeds> feeds;
			try {
				feeds = model.getFeeds(user.getId());
			} catch (IllegalAccessException e) {
				return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
			}
			ListDTO<FeedDTO> ret = new ListDTO<>();
			if (feeds != null && feeds.size() > 0) {
				List<FeedDTO> data = new ArrayList<>();
				for (Feeds f : feeds)
					data.add(new FeedDTO(f));
				ret.setData(data);
			}
			return ResponceUtils.makeResponce(ret);
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_ADD, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoAdd(HttpServletRequest request, @RequestParam(required = true) String url) {
		logger.info("Adding new feed");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			try {
				model.addFeed(user, url);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(false));
			}
			return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(true));
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_GET_UNREAD, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoGetUnread(HttpServletRequest request, @RequestParam(required = true) Long timestamp) {
		logger.info("Getting unread feeds");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			List<Feeds> feeds;
			try {
				feeds = model.getFeeds(user.getId());
			} catch (IllegalAccessException e) {
				return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
			}
			ListDTO<FeedPostDTO> ret = new ListDTO<>();
			if (feeds != null && feeds.size() > 0) {
				List<Feedposts> posts = new ArrayList<>();
				List<FeedPostDTO> dtos = new ArrayList<>();
				for (Feeds feed : feeds) {
					try {
						posts.addAll(model.getNewUserPosts(user.getId(), feed, new Date(timestamp)));
					} catch (IllegalAccessException e) {
						return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
					}
				}
				if (posts != null && posts.size() > 0) {
					for (Feedposts fp : posts)
						dtos.add(new FeedPostDTO(fp));
					ret.setData(dtos);
				}
			}
			return ResponceUtils.makeResponce(ret);
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_MARK_READ, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoMarkRead(HttpServletRequest request, @RequestParam(required = false) Integer feed,
			@RequestParam(required = false) Integer post, @RequestParam(required = false, defaultValue = "false") Boolean all,
			@RequestParam(required = true) Boolean read) {
		logger.info("Marking read");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			if (feed != null) {
				try {
					model.setFeedRead(feed, read);
				} catch (IllegalAccessException e) {
					return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(false));
				}
				return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(true));
			} else if (post != null) {
				try {
					model.setPostRead(post, read);
				} catch (IllegalAccessException e) {
					return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(false));
				}
				return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(true));
			} else if (all) {
				try {
					model.setAllRead(user.getId());
				} catch (IllegalAccessException e) {
					return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(false));
				}
				return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(true));
			} else
				return ResponceUtils.makeResponce(new SimpleDataDTO<Boolean>(false));
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_UPDATE, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoUpdate(HttpServletRequest request) {
		logger.info("Updating feeds");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			List<Feeds> feeds;
			try {
				feeds = model.getFeeds(user.getId());
			} catch (IllegalAccessException e) {
				return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
			}
			Integer updated = 0;
			if (feeds != null && feeds.size() > 0) {
				for (Feeds f : feeds) {
					try {
						updated += model.updateFeed(f);
					} catch (IllegalAccessException e) {
						return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
					}
				}
			}
			return ResponceUtils.makeResponce(new SimpleDataDTO<Integer>(updated));
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_DEL, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoDel(HttpServletRequest request) {
		logger.info("Deleting feed");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
		return ResponceUtils.makeResponce("");
	}

	@RequestMapping(value = URLConstants.DoJson.Rss.RSS_DO_GET_FEED, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> rssDoGetFeed(HttpServletRequest request, @RequestParam(required = true) Integer count, @RequestParam Integer id) {
		logger.info("Getting feed");
		User user = SessionHelper.getCurrentIUser();
		if (user != null) {
			ListDTO<FeedPostDTO> ret = new ListDTO<>();
			List<Feedposts> posts = new ArrayList<>();
			List<FeedPostDTO> dtos = new ArrayList<>();
			posts.addAll(model.getFeedPosts(id, 0, count));
			if (posts != null && posts.size() > 0) {
				for (Feedposts fp : posts)
					dtos.add(new FeedPostDTO(fp));
				ret.setData(dtos);
			}
			return ResponceUtils.makeResponce(ret);
		} else {
			return ResponceUtils.makeErrorResponce(ErrorConstants.ERR_NOT_AUTHORIZED_MSG, ErrorConstants.ERR_NOT_AUTHORIZED_ID);
		}
	}

}
