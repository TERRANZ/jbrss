package ru.terra.jbrss.db.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.terra.jbrss.db.entity.base.Feeds;

import java.util.List;

@NoRepositoryBean
public interface FeedsRepository extends JpaRepository<Feeds, Integer> {
    List<Feeds> findByFeedurl(String url);
}
