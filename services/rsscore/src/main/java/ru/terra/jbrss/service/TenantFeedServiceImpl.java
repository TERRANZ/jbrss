package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.FeedsRepository;

import java.util.List;

@Service("tenantFeedsService")
public class TenantFeedServiceImpl implements FeedService {

    @Autowired
    private FeedsRepository repository;

    @Override
    public List<Feeds> getFeeds() {
        return repository.findAll();
    }
}
