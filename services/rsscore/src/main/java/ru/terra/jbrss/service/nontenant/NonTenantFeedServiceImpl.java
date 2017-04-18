package ru.terra.jbrss.service.nontenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.terra.jbrss.db.entity.BaseFeeds;
import ru.terra.jbrss.db.entity.nontenant.NonTenantFeeds;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedsRepository;
import ru.terra.jbrss.service.FeedService;

import java.util.List;

@Service("nonTenantFeedsService")
public class NonTenantFeedServiceImpl implements FeedService<NonTenantFeeds> {

    @Autowired
    private NonTenantFeedsRepository repository;

    @Override
    @Transactional(transactionManager = "nonTenantTransactionManager")
    public List<NonTenantFeeds> getFeeds() {
        return repository.findAll();
    }
}
