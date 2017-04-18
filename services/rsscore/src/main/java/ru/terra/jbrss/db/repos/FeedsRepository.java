package ru.terra.jbrss.db.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.terra.jbrss.db.entity.base.BaseFeeds;

import java.util.List;

@NoRepositoryBean
public interface FeedsRepository<T extends BaseFeeds> extends JpaRepository<T, Integer> {
    List<T> findByFeedurl(String url);
}
