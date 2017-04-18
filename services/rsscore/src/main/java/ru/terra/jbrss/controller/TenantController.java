package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.service.NonTenantFeedServiceImpl;
import ru.terra.jbrss.service.TenantFeedServiceImpl;
import ru.terra.jbrss.shared.dto.FeedDto;
import ru.terra.jbrss.shared.dto.FeedListDto;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/test/{uid}")
public class TenantController {

    @Autowired
    private NonTenantFeedServiceImpl nonTenantfeedService;
    @Autowired
    private TenantFeedServiceImpl tenantFeedService;

    //    @Transactional
    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedListDto> allFeeds() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            FeedListDto feedListDto = new FeedListDto();
            feedListDto.data = nonTenantfeedService.getFeeds().stream().map(feed -> {
                FeedDto feedDto = new FeedDto();
                feedDto.setId(feed.getId());
                feedDto.setFeedname(feed.getFeedname());
                feedDto.setFeedurl(feed.getFeedurl());
                feedDto.setUpdateTime(feed.getUpdateTime().getTime());
                return feedDto;
            }).collect(Collectors.toList());
            feedListDto.data.addAll(tenantFeedService.getFeeds().stream().map(feed -> {
                FeedDto feedDto = new FeedDto();
                feedDto.setId(feed.getId());
                feedDto.setFeedname(feed.getFeedname());
                feedDto.setFeedurl(feed.getFeedurl());
                feedDto.setUpdateTime(feed.getUpdateTime().getTime());
                return feedDto;
            }).collect(Collectors.toList()));
            return ResponseEntity.ok(feedListDto);
        }
    }
}
