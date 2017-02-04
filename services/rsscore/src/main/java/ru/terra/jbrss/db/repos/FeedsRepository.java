package ru.terra.jbrss.db.repos;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.terra.jbrss.db.entity.Feeds;

import java.util.List;

public interface FeedsRepository extends PagingAndSortingRepository<Feeds, Integer> {
    List<Feeds> findByUserid(Integer usedId);

    List<Feeds> findByUseridAndByFeedId(Integer usedid, Integer id);

    List<Feeds> findByUseridAndByFeedURL(Integer usedid, String url);
}
