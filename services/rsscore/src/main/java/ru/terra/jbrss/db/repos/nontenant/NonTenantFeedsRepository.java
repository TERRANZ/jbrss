package ru.terra.jbrss.db.repos.nontenant;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.terra.jbrss.db.entity.nontenant.NonTenantFeeds;

import java.util.List;

public interface NonTenantFeedsRepository extends JpaRepository<NonTenantFeeds, Integer> {
    List<NonTenantFeeds> findByFeedurl(String url);
}
