package ru.terra.jbrss.service.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.tenant.TenantFeeds;
import ru.terra.jbrss.db.repos.tenant.FeedsRepository;
import ru.terra.jbrss.service.FeedService;

import java.util.List;

@Service("tenantFeedsService")
public class TenantFeedServiceImpl implements FeedService<TenantFeeds> {

    @Autowired
    private FeedsRepository repository;

    @Override
    public List<TenantFeeds> getFeeds() {
        return repository.findAll();
    }
}
