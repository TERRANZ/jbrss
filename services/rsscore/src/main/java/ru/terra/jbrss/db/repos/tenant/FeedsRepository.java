package ru.terra.jbrss.db.repos.tenant;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.terra.jbrss.db.entity.BaseFeeds;
import ru.terra.jbrss.db.entity.tenant.TenantFeeds;

import java.util.List;

public interface FeedsRepository extends JpaRepository<TenantFeeds, Integer> {
    List<TenantFeeds> findByFeedurl(String url);
}
