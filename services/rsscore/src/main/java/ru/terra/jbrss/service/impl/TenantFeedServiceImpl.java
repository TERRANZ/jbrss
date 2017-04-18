package ru.terra.jbrss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.tenant.TenantFeedsRepository;
import ru.terra.jbrss.service.FeedService;

import java.util.List;

@Service("tenantFeedsService")
public class TenantFeedServiceImpl implements FeedService {

    @Autowired
    private TenantFeedsRepository repository;

    @Override
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
