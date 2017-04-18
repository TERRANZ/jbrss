package ru.terra.jbrss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedsRepository;
import ru.terra.jbrss.service.FeedService;

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

    @Override
    public Feeds findOne(Integer fid) {
        return repository.findOne(fid);
    }

    @Override
    public List<Feeds> findByFeedURL(String url) {
        return repository.findByFeedurl(url);
    }

    @Override
    public void save(Feeds feeds) {
        repository.save(feeds);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
