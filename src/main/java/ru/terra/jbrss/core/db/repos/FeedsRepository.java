package ru.terra.jbrss.core.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Feeds;

import java.util.List;

public interface FeedsRepository extends CrudRepository<Feeds, Integer> {
    List<Feeds> findByUserid(Integer usedId);

    List<Feeds> findByUseridAndByFeedId(Integer usedid, Integer id);

    List<Feeds> findByUseridAndByFeedURL(Integer usedid, String url);
}
