package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.terra.jbrss.db.entity.base.Feeds;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedsRepository;

import java.util.List;

@Service("nonTenantFeedsService")
public class NonTenantFeedServiceImpl implements FeedService {

    @Autowired
    private NonTenantFeedsRepository repository;

    @Override
    @Transactional(transactionManager = "nonTenantTransactionManager")
    public List<Feeds> getFeeds() {
        return repository.findAll();
    }
}
