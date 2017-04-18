package ru.terra.jbrss.db.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.terra.jbrss.db.entity.Feeds;

import java.util.List;

@Repository
public interface FeedsRepository extends JpaRepository<Feeds, Integer> {
    List<Feeds> findByFeedurl(String url);
}
