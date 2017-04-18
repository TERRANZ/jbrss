package ru.terra.jbrss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedPostsRepository;
import ru.terra.jbrss.service.FeedPostsService;

import java.util.List;

@Service("nonTenantFeedPostsService")
public class NonTenantFeedPostsServiceImpl implements FeedPostsService {
    @Autowired
    NonTenantFeedPostsRepository repository;

    @Override
    public List<Feedposts> findFeedpostsByFeedLimited(Integer fid, Integer offset, Integer limit) {
        return repository.findFeedpostsByFeedLimited(fid, offset, limit);
    }

    @Override
    public Integer countByFeedId(Integer fid) {
        return repository.countByFeedId(fid);
    }

    @Override
    public List<Feedposts> getLastPostInFeed(Integer id) {
        return repository.getLastPostInFeed(id);
    }

    @Override
    public void save(List<Feedposts> newPosts) {
        repository.save(newPosts);
    }

    @Override
    public List<Feedposts> findByFeedId(Integer id) {
        return repository.findByFeedId(id);
    }

    @Override
    public void delete(Feedposts fp) {
        repository.delete(fp);
    }

    @Override
    public List<Feedposts> findByPosttextLike(String posttext) {
        return repository.findByPosttextLike(posttext);
    }

    @Override
    public Feedposts findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Feedposts save(Feedposts feedposts) {
        return repository.save(feedposts);
    }
}
