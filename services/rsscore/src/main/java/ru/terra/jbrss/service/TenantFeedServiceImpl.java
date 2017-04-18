package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.base.BaseFeeds;
import ru.terra.jbrss.db.entity.tenant.TenantFeeds;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.db.repos.tenant.TenantFeedsRepository;

import java.util.List;

@Service("tenantFeedsService")
public class TenantFeedServiceImpl implements FeedService<TenantFeeds> {

    @Autowired
    private TenantFeedsRepository repository;

    @Override
    public List<TenantFeeds> getFeeds() {
        return repository.findAll();
    }
}
