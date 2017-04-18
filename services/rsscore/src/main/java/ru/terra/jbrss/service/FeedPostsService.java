package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.Feedposts;

import java.util.List;

public interface FeedPostsService {

    List<Feedposts> findFeedpostsByFeedLimited(Integer fid, Integer offset, Integer limit);

    Integer countByFeedId(Integer fid);

    List<Feedposts> getLastPostInFeed(Integer id);

    void save(List<Feedposts> newPosts);

    List<Feedposts> findByFeedId(Integer id);

    void delete(Feedposts fp);

    List<Feedposts> findByPosttextLike(String posttext);

    Feedposts findOne(Integer id);

    void deleteAll();

    Feedposts save(Feedposts feedposts);
}
