package ru.terra.jbrss.db.repos;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.terra.jbrss.db.entity.Feeds;

import java.util.List;

public interface FeedsRepository extends PagingAndSortingRepository<Feeds, Integer> {
    List<Feeds> findByUserid(String userId);

    List<Feeds> findByUseridAndByFeedId(String userId, Integer id);

    List<Feeds> findByUseridAndByFeedURL(String userId, String url);
}
