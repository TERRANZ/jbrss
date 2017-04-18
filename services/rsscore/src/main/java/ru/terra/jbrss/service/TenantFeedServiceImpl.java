package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.base.Feeds;
import ru.terra.jbrss.db.repos.tenant.TenantFeedsRepository;

import java.util.List;

@Service("tenantFeedsService")
public class TenantFeedServiceImpl implements FeedService {

    @Autowired
    private TenantFeedsRepository repository;

    @Override
    public List<Feeds> getFeeds() {
        return repository.findAll();
    }
}
