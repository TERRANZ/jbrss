package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.db.repos.FeedsRepository;

@Controller
@RequestMapping("/test/{uid}")
public class TestController {


    @Autowired
    private FeedsRepository feedsRepository;

    @RequestMapping("/")
    public
    @ResponseBody
    ResponseEntity<Long> test() {
        return ResponseEntity.ok(feedsRepository.count());
    }
}
